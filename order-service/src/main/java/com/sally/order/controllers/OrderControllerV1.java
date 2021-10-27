package com.sally.order.controllers;

import static com.sally.order.OrderServiceEndpoints.ORDER;
import static com.sally.order.OrderServiceEndpoints.ORDER_CANCEL;
import static com.sally.order.OrderServiceEndpoints.V1;

import com.sally.api.commands.CancelOrderCommand;
import com.sally.api.commands.CreateOrderCommand;
import com.sally.api.requests.CreateOrderRequest;
import com.sally.auth.SalyUserDetails;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequestMapping(V1)
@RestController
public class OrderControllerV1 {

    private final CommandGateway commandGateway;

    public OrderControllerV1(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping(ORDER)
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<String> placeOrder(@AuthenticationPrincipal SalyUserDetails userDetails, @RequestBody CreateOrderRequest request) {
        final CreateOrderCommand command = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID())
                .customerId(userDetails.getUserId())
                .customerName(userDetails.getUsername())
                .items(CreateOrderCommand.Item.fromRequest(request.getOrderItems()))
                .build();

        return commandGateway.send(command);
    }

    @PostMapping(ORDER_CANCEL)
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<String> cancelOrder(@AuthenticationPrincipal SalyUserDetails userDetails, @RequestBody CancelOrderCommand request) {
        final CancelOrderCommand command = CancelOrderCommand.builder()
                .customerId(userDetails.getUserId())
                .orderId(request.getOrderId())
                .build();

        return commandGateway.send(command);
    }


}
