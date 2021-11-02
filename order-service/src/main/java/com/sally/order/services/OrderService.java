package com.sally.order.services;

import com.sally.api.Order;
import com.sally.api.requests.CreateOrderRequest;
import com.sally.auth.SalyUserDetails;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<Order> placeOrder(SalyUserDetails userDetails, CreateOrderRequest request);

    CompletableFuture<Void> cancelOrder(UUID orderId, SalyUserDetails userDetails);

    List<Order> getCustomerOrders(SalyUserDetails userDetails);
}
