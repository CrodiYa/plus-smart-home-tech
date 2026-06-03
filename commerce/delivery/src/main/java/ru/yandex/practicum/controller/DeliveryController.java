package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.delivery.controller.DeliveryClient;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryClient {

    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @Override
    public void successfulDelivery(UUID deliveryId) {
        deliveryService.successfulDelivery(deliveryId);
    }

    @Override
    public void pickedDelivery(UUID deliveryId) {
        deliveryService.pickedDelivery(deliveryId);
    }

    @Override
    public void failedDelivery(UUID deliveryId) {
        deliveryService.failedDelivery(deliveryId);
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        return deliveryService.calculateTotalCost(orderDto);
    }
}
