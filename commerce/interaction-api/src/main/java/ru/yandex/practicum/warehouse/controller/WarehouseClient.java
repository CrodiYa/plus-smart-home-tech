package ru.yandex.practicum.warehouse.controller;

import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.cart.dto.BookedProductsDto;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.warehouse.dto.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient {

    @PutMapping
    void addNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request) throws FeignException;

    @PostMapping("/check")
    BookedProductsDto checkQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto) throws FeignException;

    @PostMapping("/add")
    void addProduct(@RequestBody @Valid AddProductToWarehouseRequest request) throws FeignException;

    @GetMapping("/address")
    AddressDto getAddress();

    @PostMapping("/shipped")
    void shipped(@RequestBody @Valid ShippedDeliveryRequest request);

    @PostMapping("/assembly")
    BookedProductsDto assembly(@RequestBody @Valid AssemblyProductsForOrderRequest request);

    @PostMapping("/return")
    void returnProducts(@RequestBody @NotNull Map<UUID, Integer> products);
}
