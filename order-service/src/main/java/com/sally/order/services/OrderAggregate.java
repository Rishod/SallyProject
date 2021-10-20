package com.sally.order.services;

import com.sally.api.OrderItem;
import com.sally.api.OrderStatus;
import com.sally.api.commands.CancelOrderCommand;
import com.sally.api.commands.CreateOrderCommand;
import com.sally.api.commands.UpdateOrderItemsCommand;
import com.sally.api.commands.UpdateOrderStatusCommand;
import com.sally.api.events.OrderCanceledEvent;
import com.sally.api.events.OrderCreatedEvent;
import com.sally.api.events.OrderItemsUpdatedEvent;
import com.sally.api.events.OrderStatusUpdatedEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Aggregate
public class OrderAggregate {
    @AggregateIdentifier
    private UUID orderId;
    private UUID customerId;
    private List<OrderItem> orderItems;
    private BigDecimal total;
    private OrderStatus orderStatus;

    public OrderAggregate() {
        // for Axon
    }

    @CommandHandler
    public OrderAggregate(final CreateOrderCommand command) {
        final OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(command.getOrderId())
                .customerId(command.getCustomerId())
                .orderItems(OrderItem.ofCommandItem(command.getItems()))
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void on(final UpdateOrderItemsCommand command) {
        final OrderItemsUpdatedEvent event = OrderItemsUpdatedEvent.builder()
                .customerId(this.customerId)
                .id(this.orderId)
                .orderItems(command.getItems())
                .total(command.getTotal())
                .build();

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void on(final UpdateOrderStatusCommand command) {
        AggregateLifecycle.apply(new OrderStatusUpdatedEvent(command.getOrderId(), command.getOrderStatus()));
    }

    @CommandHandler
    public void on(final CancelOrderCommand command) {
        AggregateLifecycle.apply(new OrderCanceledEvent(command.getOrderId(), command.getCustomerId()));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.customerId = event.getCustomerId();
        this.orderId = event.getOrderId();
        this.orderItems = event.getOrderItems();
        this.orderStatus = OrderStatus.VERIFYING;
    }

    @EventSourcingHandler
    public void on(OrderItemsUpdatedEvent event) {
        this.orderItems = event.getOrderItems();
        this.total = event.getTotal();
    }

    @EventSourcingHandler
    public void on(OrderStatusUpdatedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(final OrderCanceledEvent event) {
        this.orderStatus = OrderStatus.PLACED;
    }

}
