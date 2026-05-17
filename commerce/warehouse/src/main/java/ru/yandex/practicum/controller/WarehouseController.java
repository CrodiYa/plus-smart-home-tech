package ru.yandex.practicum.controller;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.cart.dto.BookedProductsDto;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.service.WarehouseService;
import ru.yandex.practicum.warehouse.controller.WarehouseClient;
import ru.yandex.practicum.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.warehouse.dto.AddressDto;
import ru.yandex.practicum.warehouse.dto.NewProductInWarehouseRequest;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {

    private final WarehouseService warehouseService;

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) throws FeignException {
        warehouseService.addNewProduct(request);
    }

    @Override
    public BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto) throws FeignException {
        return warehouseService.checkQuantity(shoppingCartDto);
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) throws FeignException {
        warehouseService.addProduct(request);
    }

    @Override
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}
