package ru.yandex.practicum.order.controller;

import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {
    @GetMapping
    List<OrderDto> getOrders(@RequestParam @NotNull String username) throws FeignException;

    @PutMapping
    OrderDto addOrder(@RequestBody @Valid CreateNewOrderRequest request) throws FeignException;

    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest request) throws FeignException;

    @PostMapping("/payment")
    OrderDto orderPaid(@RequestBody @NotNull UUID orderId) throws FeignException;

    @PostMapping("/payment/failed")
    OrderDto failedOrderPayment(@RequestBody @NotNull UUID orderId) throws FeignException;

    @PostMapping("/delivery")
    OrderDto orderDelivered(@RequestBody @NotNull UUID orderId) throws FeignException;

    @PostMapping("/delivery/failed")
    OrderDto failedOrderDelivery(@RequestBody @NotNull UUID orderId) throws FeignException;

    @PostMapping("/completed")
    OrderDto orderCompleted(@RequestBody @NotNull UUID orderId) throws FeignException;

    @PostMapping("/calculate/total")
    OrderDto calculateOrderTotal(@RequestBody @NotNull UUID orderId) throws FeignException;

    @PostMapping("/calculate/delivery")
    OrderDto calculateOrderDelivery(@RequestBody @NotNull UUID orderId) throws FeignException;

    @PostMapping("/assembly")
    OrderDto orderAssembled(@RequestBody @NotNull UUID orderId) throws FeignException;

    @PostMapping("/assembly/failed")
    OrderDto failedOrderAssemble(@RequestBody @NotNull UUID orderId) throws FeignException;
}
