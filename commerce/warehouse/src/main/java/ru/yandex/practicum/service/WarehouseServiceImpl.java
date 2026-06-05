package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.cart.dto.BookedProductsDto;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.OrderBooking;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.BookingRepository;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.warehouse.dto.*;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.NotEnoughProductsException;
import ru.yandex.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private static final int ARRAY_START = 0;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(ARRAY_START, ADDRESSES.length)];

    private final WarehouseRepository warehouseRepository;
    private final ProductMapper productMapper;
    private final BookingRepository bookingRepository;

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        if (warehouseRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(request.getProductId());
        }

        WarehouseProduct product = productMapper.toWarehouseProduct(request);
        warehouseRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto) {
        BookedProductsDto bookedProducts = new BookedProductsDto();

        Map<UUID, WarehouseProduct> productsById =
                warehouseRepository.findAllById(shoppingCartDto.getProducts().keySet()).stream()
                        .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        shoppingCartDto.getProducts().forEach((productId, quantity) -> {
            WarehouseProduct warehouseProduct = productsById.get(productId);

            if (warehouseProduct == null) {
                throw new NoSpecifiedProductInWarehouseException(productId);
            }

            if (warehouseProduct.getQuantity() < quantity) {
                throw new NotEnoughProductsException(productId);
            }

            bookedProducts.setFragile(bookedProducts.isFragile() || warehouseProduct.getFragile());
            bookedProducts.setDeliveryWeight(bookedProducts.getDeliveryWeight() + warehouseProduct.getWeight() * quantity);

            double volume = warehouseProduct.getWidth() * warehouseProduct.getDepth() * warehouseProduct.getHeight();
            bookedProducts.setDeliveryVolume(bookedProducts.getDeliveryVolume() + volume * quantity);
        });

        return bookedProducts;
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        WarehouseProduct warehouseProduct = warehouseRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(request.getProductId()));

        warehouseProduct.setQuantity(warehouseProduct.getQuantity() + request.getQuantity());
        warehouseRepository.save(warehouseProduct);
    }

    @Override
    public AddressDto getAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    @Override
    public void shipped(ShippedDeliveryRequest request) {
        OrderBooking booking = bookingRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("No booking found for order " + request.getOrderId()));

        booking.setDeliveryId(request.getDeliveryId());
        bookingRepository.save(booking);
    }

    @Override
    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
        Map<UUID, Integer> products = request.getProducts();
        double weight = 0;
        double volume = 0;
        boolean fragile = false;

        Map<UUID, WarehouseProduct> warehouseProducts = getWarehouseProducts(products.keySet());

        for (Map.Entry<UUID, Integer> cartProduct : products.entrySet()) {
            WarehouseProduct warehouseProduct = warehouseProducts.get(cartProduct.getKey());
            long newQuantity = warehouseProduct.getQuantity() - cartProduct.getValue();
            if (newQuantity < 0) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Not enough products");
            }
            warehouseProduct.setQuantity(newQuantity);

            weight += warehouseProduct.getWeight() * cartProduct.getValue();
            volume += warehouseProduct.getHeight() * warehouseProduct.getWeight()
                      * warehouseProduct.getDepth() * cartProduct.getValue();
            fragile = fragile || warehouseProduct.getFragile();
        }

        warehouseRepository.saveAll(warehouseProducts.values());
        saveBooked(request.getOrderId(), products);

        return new BookedProductsDto(weight, volume, fragile);
    }

    @Override
    public void returnProducts(Map<UUID, Integer> products) {
        Map<UUID, WarehouseProduct> warehouseProducts = getWarehouseProducts(products.keySet());

        for (Map.Entry<UUID, Integer> product : products.entrySet()) {
            WarehouseProduct warehouseProduct = warehouseProducts.get(product.getKey());
            warehouseProduct.setQuantity(warehouseProduct.getQuantity() + product.getValue());
        }

        warehouseRepository.saveAll(warehouseProducts.values());
    }

    private Map<UUID, WarehouseProduct> getWarehouseProducts(Collection<UUID> ids) {
        Map<UUID, WarehouseProduct> products = warehouseRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        if (products.size() != ids.size()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Not enough products");
        }

        return products;
    }

    private void saveBooked(UUID orderId, Map<UUID, Integer> products) {
        OrderBooking orderBooking = new OrderBooking();
        orderBooking.setOrderId(orderId);
        orderBooking.setProducts(products);
        bookingRepository.save(orderBooking);
    }
}
