package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.order.controller.OrderClient;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.payment.enums.PaymentStatus;
import ru.yandex.practicum.payment.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.payment.exception.PaymentNotFoundException;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.store.controller.StoreClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static ru.yandex.practicum.payment.dto.Constants.FEE_COEFFICIENT;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final StoreClient storeClient;

    @Override
    public PaymentDto startPayment(OrderDto orderDto) {

        Payment payment = paymentMapper.toPayment(orderDto);
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);

        return paymentMapper.toPaymentDto(payment);
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        BigDecimal products = orderDto.getProductPrice();
        BigDecimal delivery = orderDto.getDeliveryPrice();
        if (products == null || delivery == null) {
            throw new NotEnoughInfoInOrderToCalculateException();
        }

        products = products.add(products.multiply(FEE_COEFFICIENT));

        return products.add(delivery);
    }

    @Override
    public BigDecimal calculateProductCost(OrderDto orderDto) {

        BigDecimal totalProductsCost = BigDecimal.ZERO;

        for (Map.Entry<UUID, Integer> entry : orderDto.getProducts().entrySet()) {
            Double price = storeClient.getProduct(entry.getKey()).getPrice();
            BigDecimal total = BigDecimal.valueOf(price * entry.getValue());
            totalProductsCost = totalProductsCost.add(total);
        }

        return totalProductsCost;
    }

    @Override
    public void refundPayment(UUID paymentId) {
        Payment payment = findPaymentById(paymentId);
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        orderClient.orderPaid(payment.getOrderId());
    }

    @Override
    public void failedPayment(UUID paymentId) {
        Payment payment = findPaymentById(paymentId);
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        orderClient.failedOrderPayment(payment.getOrderId());
    }

    private Payment findPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }
}
