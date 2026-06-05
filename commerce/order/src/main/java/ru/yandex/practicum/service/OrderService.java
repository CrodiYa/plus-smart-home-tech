package ru.yandex.practicum.service;

import ru.yandex.practicum.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> getOrders(String username);

    OrderDto addOrder(CreateNewOrderRequest request);

    OrderDto returnOrder(ProductReturnRequest request);

    OrderDto orderPaid(UUID orderId);

    OrderDto failedOrderPayment(UUID orderId);

    OrderDto orderDelivered(UUID orderId);

    OrderDto failedOrderDelivery(UUID orderId);

    OrderDto completedOrder(UUID orderId);

    OrderDto calculateOrderTotal(UUID orderId);

    OrderDto calculateOrderDelivery(UUID orderId);

    OrderDto orderAssembled(UUID orderId);

    OrderDto failedOrderAssemble(UUID orderId);
}
