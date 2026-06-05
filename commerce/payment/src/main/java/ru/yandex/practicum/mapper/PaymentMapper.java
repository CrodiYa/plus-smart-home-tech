package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;

@Component
public class PaymentMapper {

    public Payment toPayment(OrderDto orderDto) {
        Payment payment = new Payment();
        payment.setOrderId(orderDto.getOrderId());
        payment.setTotalPayment(orderDto.getTotalPrice());
        payment.setDeliveryTotal(orderDto.getDeliveryPrice());
        payment.setProductTotal(orderDto.getProductPrice());
        return payment;
    }

    public PaymentDto toPaymentDto(Payment payment) {

        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getTotalPayment()
                        .subtract(payment.getProductTotal())
                        .subtract(payment.getDeliveryTotal()))
                .build();
    }
}
