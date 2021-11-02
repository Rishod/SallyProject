package com.sally.order.services;

import com.sally.api.events.OrderCreatedEvent;
import com.sally.api.events.OrderItemsUpdatedEvent;
import com.sally.api.events.OrderStatusUpdatedEvent;
import com.sally.order.dao.OrderDAO;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderProjection {
    private final OrderDAO orderDAO;

    public OrderProjection(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @EventHandler
    @Transactional
    public void on(final OrderCreatedEvent event) {
        orderDAO.create(UUID.fromString(event.getOrderId()), UUID.fromString(event.getCustomerId()), event.getOrderItems(), event.getStatus());
    }

    @EventHandler
    @Transactional
    public void on(OrderItemsUpdatedEvent event) {
        orderDAO.updateOrder(event.getId(), event.getTotal(), event.getOrderItems());
    }

    @EventHandler
    @Transactional
    public void on(OrderStatusUpdatedEvent event) {
        orderDAO.updateStatus(event.getOrderId(), event.getOrderStatus());
    }
}
