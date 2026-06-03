package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.cart.dto.BookedProductsDto;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.delivery.controller.DeliveryClient;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.delivery.enums.DeliveryState;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;
import ru.yandex.practicum.order.enums.OrderState;
import ru.yandex.practicum.order.exception.NoOrderFoundException;
import ru.yandex.practicum.payment.controller.PaymentClient;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.warehouse.controller.WarehouseClient;
import ru.yandex.practicum.warehouse.dto.AssemblyProductsForOrderRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(String username) {
        return orderRepository.findAllByUsername(username).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto addOrder(CreateNewOrderRequest request) {
        ShoppingCartDto cart = request.getShoppingCart();

        BookedProductsDto booked = warehouseClient.checkQuantity(cart);

        Order order = createOrder(request, booked);
        orderRepository.save(order);

        UUID deliveryId = createDeliveryAndGetDeliveryId(request, order);
        order.setDeliveryId(deliveryId);

        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = findOrderById(request.getOrderId());
        order.setState(OrderState.PRODUCT_RETURNED);
        orderRepository.save(order);

        warehouseClient.returnProducts(request.getProducts());

        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderPaid(UUID orderId) {
        Order order = findOrderById(orderId);
        order.setState(OrderState.PAID);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto failedOrderPayment(UUID orderId) {
        Order order = findOrderById(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderDelivered(UUID orderId) {
        Order order = findOrderById(orderId);
        order.setState(OrderState.DELIVERED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto failedOrderDelivery(UUID orderId) {
        Order order = findOrderById(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto completedOrder(UUID orderId) {
        Order order = findOrderById(orderId);
        order.setState(OrderState.COMPLETED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto calculateOrderTotal(UUID orderId) {
        Order order = findOrderById(orderId);
        BigDecimal productPrice = paymentClient.calculateProductCost(orderMapper.toOrderDto(order));
        BigDecimal total = paymentClient.calculateTotalCost(orderMapper.toOrderDto(order));

        order.setProductPrice(productPrice);
        order.setTotalPrice(total);

        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto calculateOrderDelivery(UUID orderId) {
        Order order = findOrderById(orderId);
        BigDecimal deliveryPrice = deliveryClient.calculateTotalCost(orderMapper.toOrderDto(order));
        order.setDeliveryPrice(deliveryPrice);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderAssembled(UUID orderId) {
        Order order = findOrderById(orderId);
        BookedProductsDto dto = warehouseClient.assembly(AssemblyProductsForOrderRequest.builder()
                .orderId(orderId)
                .products(order.getProducts())
                .build());

        order.setFragile(dto.isFragile());
        order.setDeliveryWeight(dto.getDeliveryWeight());
        order.setDeliveryVolume(dto.getDeliveryVolume());
        order.setState(OrderState.ASSEMBLED);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto failedOrderAssemble(UUID orderId) {
        Order order = findOrderById(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    private Order findOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NoOrderFoundException(orderId));
    }

    private Order createOrder(CreateNewOrderRequest request, BookedProductsDto booked) {
        Order order = new Order();
        order.setUsername(request.getUsername());
        order.setShoppingCartId(request.getShoppingCart().getShoppingCartId());
        order.setProducts(request.getShoppingCart().getProducts());

        order.setDeliveryWeight(booked.getDeliveryWeight());
        order.setDeliveryVolume(booked.getDeliveryVolume());
        order.setFragile(booked.isFragile());

        return order;
    }

    private UUID createDeliveryAndGetDeliveryId(CreateNewOrderRequest request, Order order) {
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .orderId(order.getOrderId())
                .deliveryState(DeliveryState.CREATED)
                .fromAddress(warehouseClient.getAddress())
                .toAddress(request.getDeliveryAddress())
                .build();

        return deliveryClient.createDelivery(deliveryDto).getDeliveryId();
    }
}
