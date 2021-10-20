package com.sally.order.services;

import com.sally.api.OrderStatus;
import com.sally.api.commands.CreateShippingCommand;
import com.sally.api.commands.UpdateOrderItemsCommand;
import com.sally.api.commands.UpdateOrderStatusCommand;
import com.sally.api.commands.VerifyProductsCommand;
import com.sally.api.events.OrderCanceledEvent;
import com.sally.api.events.OrderCreatedEvent;
import com.sally.api.events.OrderShippedEvent;
import com.sally.api.events.OrderShippingCanceledEvent;
import com.sally.api.events.ProductsVerificationFailedEvent;
import com.sally.api.events.ProductsVerificationSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderSaga {
    public static final String ORDER_ID_ASSOCIATION_KEY = "orderId";
    public static final String CUSTOMER_ID_ASSOCIATION_KEY = "customerId";

    private UUID customerId;

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION_KEY)
    public void startSaga(final OrderCreatedEvent event) {
        log.info("Starting new saga for order: {} and customer: {}", event.getOrderId(), event.getCustomerId());
        SagaLifecycle.associateWith(CUSTOMER_ID_ASSOCIATION_KEY, event.getCustomerId());

        this.customerId = UUID.fromString(event.getCustomerId());

        final VerifyProductsCommand verifyProductsCommand = VerifyProductsCommand.builder()
                .orderId(UUID.fromString(event.getOrderId()))
                .products(VerifyProductsCommand.Product.ofOrderItems(event.getOrderItems()))
                .build();

        commandGateway.send(verifyProductsCommand);
    }

    @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION_KEY)
    public void on(final ProductsVerificationFailedEvent event) {
        log.info("Handle ProductsVerificationFailedEvent OrderSaga (orderId: {})", event.getOrderId());
        commandGateway.sendAndWait(new UpdateOrderStatusCommand(UUID.fromString(event.getOrderId()), OrderStatus.VERIFICATION_FAILED));

        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION_KEY)
    public void on(final ProductsVerificationSuccessEvent event) {
        log.info("Handle ProductsVerificationSuccessEvent (orderId: {})", event.getOrderId());
        final UUID orderId = UUID.fromString(event.getOrderId());

        commandGateway.send(new UpdateOrderItemsCommand(orderId, event.getVerifiedProducts(), event.getTotal()))
                .thenCompose(result -> commandGateway.send(new UpdateOrderStatusCommand(orderId, OrderStatus.SHIPPING)))
                .thenCompose(result -> commandGateway.send(new CreateShippingCommand(orderId, customerId, event.getVerifiedProducts())));
    }

    @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION_KEY)
    public void on(final OrderShippedEvent event) {
        log.info("Handle OrderShippedEvent (orderId: {})", event.getOrderId());
        commandGateway.sendAndWait(new UpdateOrderStatusCommand(UUID.fromString(event.getOrderId()), OrderStatus.SHIPPED));

        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION_KEY)
    public void on(final OrderShippingCanceledEvent event) {
        log.info("Handle OrderShippingCanceled  (orderId: {})", event.getOrderId());
        commandGateway.sendAndWait(new UpdateOrderStatusCommand(UUID.fromString(event.getOrderId()), OrderStatus.REJECTED_BY_SHOP));

        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION_KEY)
    public void on(final OrderCanceledEvent event) {
        log.info("Handle OrderCanceledEvent  (orderId: {})", event.getOrderId());
        commandGateway.sendAndWait(new UpdateOrderStatusCommand(UUID.fromString(event.getOrderId()), OrderStatus.CANCELED_BY_CUSTOMER));

        SagaLifecycle.end();
    }
}
