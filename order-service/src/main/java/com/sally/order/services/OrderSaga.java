package com.sally.order.services;

import com.sally.api.commands.VerifyProductsCommand;
import com.sally.api.events.OrderCreatedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void startSaga(final OrderCreatedEvent event) {
        final VerifyProductsCommand verifyProductsCommand = VerifyProductsCommand.builder()
                .orderId(event.getOrderId())
                .products(VerifyProductsCommand.Product.ofOrderItems(event.getOrderItems()))
                .build();

        commandGateway.send(verifyProductsCommand);
    }

    /**
     * Steps
     *
     */



}
