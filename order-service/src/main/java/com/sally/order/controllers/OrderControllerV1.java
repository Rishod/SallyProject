package com.sally.order.controllers;

import static com.sally.order.OrderServiceEndpoints.ORDER;
import static com.sally.order.OrderServiceEndpoints.ORDER_CANCEL;
import static com.sally.order.OrderServiceEndpoints.V1;

import com.sally.api.Order;
import com.sally.api.requests.CreateOrderRequest;
import com.sally.auth.SalyUserDetails;
import com.sally.order.services.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequestMapping(V1)
@RestController
public class OrderControllerV1 {

    private final OrderService orderService;

    public OrderControllerV1(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(ORDER)
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<Order> placeOrder(@AuthenticationPrincipal SalyUserDetails userDetails, @RequestBody CreateOrderRequest request) {
        return orderService.placeOrder(userDetails, request);
    }

    @PostMapping(ORDER_CANCEL)
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<Void> cancelOrder(@AuthenticationPrincipal SalyUserDetails userDetails, @PathVariable("id") final UUID id) {
        return orderService.cancelOrder(id, userDetails);
    }

    @GetMapping(ORDER)
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Order> getCustomerOrders(@AuthenticationPrincipal SalyUserDetails userDetails) {
        return orderService.getCustomerOrders(userDetails);
    }
}
