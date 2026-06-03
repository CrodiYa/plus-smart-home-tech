package ru.yandex.practicum.service;

import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    PaymentDto startPayment(OrderDto orderDto);

    BigDecimal calculateTotalCost(OrderDto orderDto);

    BigDecimal calculateProductCost(OrderDto orderDto);

    void refundPayment(UUID paymentId);

    void failedPayment(UUID paymentId);
}
