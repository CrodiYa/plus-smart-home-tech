package ru.yandex.practicum.payment.controller;

import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto startPayment(@RequestBody @Valid OrderDto orderDto) throws FeignException;

    @PostMapping("/totalCost")
    BigDecimal calculateTotalCost(@RequestBody @Valid OrderDto orderDto) throws FeignException;

    @PostMapping("/productCost")
    BigDecimal calculateProductCost(@RequestBody @Valid OrderDto orderDto) throws FeignException;

    @PostMapping("/refund")
    void refundPayment(@RequestBody @NotNull UUID paymentId) throws FeignException;

    @PostMapping("/failed")
    void failedPayment(@RequestBody @NotNull UUID paymentId) throws FeignException;
}
