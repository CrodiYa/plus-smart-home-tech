package ru.yandex.practicum.delivery.controller;

import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {

    @PutMapping
    DeliveryDto createDelivery(@RequestBody @Valid DeliveryDto deliveryDto) throws FeignException;

    @PostMapping("/successful")
    void successfulDelivery(@RequestBody @NotNull UUID deliveryId) throws FeignException;

    @PostMapping("/picked")
    void pickedDelivery(@RequestBody @NotNull UUID deliveryId) throws FeignException;

    @PostMapping("/failed")
    void failedDelivery(@RequestBody @NotNull UUID deliveryId) throws FeignException;

    @PostMapping("/cost")
    BigDecimal calculateTotalCost(@RequestBody @Valid OrderDto orderDto) throws FeignException;
}
