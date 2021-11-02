package com.sally.order.dao;

import com.sally.api.OrderItem;
import com.sally.api.OrderStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface OrderDAO {
    List<OrderEntity> getAllForCustomerId(UUID userId);

    List<OrderItemEntity> getOrderItems(UUID orderId);

    void create(UUID orderId, UUID customerId, List<OrderItem> items, OrderStatus status);

    void updateOrder(UUID id, BigDecimal total, List<OrderItem> items);

    void updateStatus(UUID orderId, OrderStatus orderStatus);
}
