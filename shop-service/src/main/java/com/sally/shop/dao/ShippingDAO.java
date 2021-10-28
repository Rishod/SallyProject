package com.sally.shop.dao;

import com.sally.api.OrderItem;
import com.sally.api.ShippingStatus;
import com.sally.shop.dao.entity.ShippingEntity;
import com.sally.shop.dao.entity.ShippingItemEntity;

import java.util.List;
import java.util.UUID;

public interface ShippingDAO {
    void saveShipping(UUID shopId, UUID orderId, UUID customerId, String customerName, List<OrderItem> orderItems);

    ShippingEntity updateStatus(UUID shopId, UUID shippingId, ShippingStatus delivered);

    List<ShippingEntity> getAllForShop(UUID shopId);

    List<ShippingItemEntity> getItemsForShipping(UUID shippingId);
}
