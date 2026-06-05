package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.controller.PaymentClient;
import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentClient {

    private final PaymentService paymentService;

    @Override
    public PaymentDto startPayment(OrderDto orderDto) {
        return paymentService.startPayment(orderDto);
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        return paymentService.calculateProductCost(orderDto);
    }

    @Override
    public BigDecimal calculateProductCost(OrderDto orderDto) {
        return paymentService.calculateProductCost(orderDto);
    }

    @Override
    public void refundPayment(UUID paymentId) {
        paymentService.refundPayment(paymentId);
    }

    @Override
    public void failedPayment(UUID paymentId) {
        paymentService.failedPayment(paymentId);
    }
}
