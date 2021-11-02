package com.sally.order.services;

import com.sally.api.Order;
import com.sally.api.OrderItem;
import com.sally.api.commands.CancelOrderCommand;
import com.sally.api.commands.CreateOrderCommand;
import com.sally.api.requests.CreateOrderRequest;
import com.sally.auth.SalyUserDetails;
import com.sally.order.dao.OrderDAO;
import com.sally.order.dao.OrderEntity;
import com.sally.order.dao.OrderItemEntity;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final CommandGateway commandGateway;
    private final OrderDAO orderDAO;

    public OrderServiceImpl(CommandGateway commandGateway, OrderDAO orderDAO) {
        this.commandGateway = commandGateway;
        this.orderDAO = orderDAO;
    }

    @Override
    public CompletableFuture<Order> placeOrder(SalyUserDetails userDetails, CreateOrderRequest request) {
        final CreateOrderCommand command = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID())
                .customerId(userDetails.getUserId())
                .customerName(userDetails.getUsername())
                .items(CreateOrderCommand.Item.fromRequest(request.getOrderItems()))
                .build();

        return commandGateway.send(command);
    }

    @Override
    public CompletableFuture<Void> cancelOrder(UUID orderId, SalyUserDetails userDetails) {
        final CancelOrderCommand command = CancelOrderCommand.builder()
                .customerId(userDetails.getUserId())
                .orderId(orderId)
                .build();

        return commandGateway.send(command);
    }

    @Override
    @Transactional
    public List<Order> getCustomerOrders(SalyUserDetails userDetails) {
        return orderDAO.getAllForCustomerId(userDetails.getUserId())
                .stream()
                .map(this::toOrder)
                .collect(Collectors.toList());
    }

    private Order toOrder(OrderEntity orderEntity) {
        return Order.builder()
                .id(orderEntity.getId())
                .customerId(orderEntity.getCustomerId())
                .total(orderEntity.getTotal())
                .status(orderEntity.getStatus())
                .orderItems(orderDAO.getOrderItems(orderEntity.getId()).stream().map(this::toOrderItem).collect(Collectors.toList()))
                .build();
    }

    private OrderItem toOrderItem(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
                .productId(orderItemEntity.getProductId())
                .count(orderItemEntity.getCount())
                .productName(orderItemEntity.getProductName())
                .price(orderItemEntity.getPrice())
                .build();
    }
}
