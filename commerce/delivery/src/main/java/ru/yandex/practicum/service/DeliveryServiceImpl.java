package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.NoDeliveryFoundException;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.delivery.enums.DeliveryState;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.order.controller.OrderClient;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.warehouse.controller.WarehouseClient;
import ru.yandex.practicum.warehouse.dto.ShippedDeliveryRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.yandex.practicum.delivery.dto.Constants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryMapper deliveryMapper;
    private final AddressMapper addressMapper;
    private final DeliveryRepository deliveryRepository;
    private final WarehouseClient warehouseClient;
    private final OrderClient orderClient;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {

        Delivery delivery = deliveryMapper.toDelivery(deliveryDto);
        delivery.setDeliveryState(DeliveryState.CREATED);
        deliveryRepository.save(delivery);

        return deliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    public void successfulDelivery(UUID deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);

        orderClient.orderDelivered(delivery.getOrderId());
    }

    @Override
    public void pickedDelivery(UUID deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);

        ShippedDeliveryRequest request = ShippedDeliveryRequest.builder()
                .deliveryId(deliveryId)
                .orderId(delivery.getOrderId())
                .build();

        warehouseClient.shipped(request);
    }

    @Override
    public void failedDelivery(UUID deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);

        orderClient.failedOrderDelivery(delivery.getOrderId());
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        Delivery delivery = findDeliveryById(orderDto.getDeliveryId());

        BigDecimal base = BASE_DELIVERY_RATE;
        BigDecimal total = base;
        if (warehouseClient.getAddress().getCity().contains(FIRST_ADDRESS)) {
            base = base.multiply(FIRST_ADDRESS_COEFFICIENT);
        } else if (warehouseClient.getAddress().getCity().contains(SECOND_ADDRESS)) {
            base = base.multiply(SECOND_ADDRESS_COEFFICIENT);
        }
        total = total.add(base);

        if (orderDto.isFragile()) {
            total = total.add(base.multiply(FRAGILE_COEFFICIENT));
        }
        total = total.add(BigDecimal.valueOf(orderDto.getDeliveryWeight()).multiply(WEIGHT_COEFFICIENT));
        total = total.add(BigDecimal.valueOf(orderDto.getDeliveryVolume()).multiply(VOLUME_COEFFICIENT));

        if (!warehouseClient.getAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            return total.add(total.multiply(SAME_ADDRESS_COEFFICIENT));
        }

        return total;
    }

    private Delivery findDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(() -> new NoDeliveryFoundException(deliveryId));
    }
}
