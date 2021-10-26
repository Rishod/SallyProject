package com.sally.shop.service;

import com.sally.api.OrderItem;
import com.sally.api.Shipping;

import java.util.List;
import java.util.UUID;

public interface ShippingService {
    void createShipping(UUID shopId, UUID orderId, UUID customerId, String customerName, List<OrderItem> orderItems);

    void deliverShipping(UUID shopId, UUID shippingId);

    void deniedShipping(UUID shopId, UUID shippingId);

    List<Shipping> getAllShippingsForShop(UUID shopId);

}
