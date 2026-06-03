package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.order.controller.OrderClient;
import ru.yandex.practicum.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderClient {

    private final OrderService orderService;

    @Override
    public List<OrderDto> getOrders(String username) {
        return orderService.getOrders(username);
    }

    @Override
    public OrderDto addOrder(CreateNewOrderRequest request) {
        return orderService.addOrder(request);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        return orderService.returnOrder(request);
    }

    @Override
    public OrderDto orderPaid(UUID orderId) {
        return orderService.orderPaid(orderId);
    }

    @Override
    public OrderDto failedOrderPayment(UUID orderId) {
        return orderService.failedOrderPayment(orderId);
    }

    @Override
    public OrderDto orderDelivered(UUID orderId) {
        return orderService.orderDelivered(orderId);
    }

    @Override
    public OrderDto failedOrderDelivery(UUID orderId) {
        return orderService.failedOrderDelivery(orderId);
    }

    @Override
    public OrderDto orderCompleted(UUID orderId) {
        return orderService.completedOrder(orderId);
    }

    @Override
    public OrderDto calculateOrderTotal(UUID orderId) {
        return orderService.calculateOrderTotal(orderId);
    }

    @Override
    public OrderDto calculateOrderDelivery(UUID orderId) {
        return orderService.calculateOrderDelivery(orderId);
    }

    @Override
    public OrderDto orderAssembled(UUID orderId) {
        return orderService.orderAssembled(orderId);
    }

    @Override
    public OrderDto failedOrderAssemble(UUID orderId) {
        return orderService.failedOrderAssemble(orderId);
    }
}
