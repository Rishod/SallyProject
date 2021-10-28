package com.sally.shop.service;

import com.sally.api.OrderItem;
import com.sally.api.Shipping;
import com.sally.api.ShippingItem;
import com.sally.api.ShippingStatus;
import com.sally.api.events.OrderShippedEvent;
import com.sally.api.events.OrderShippingCanceledEvent;
import com.sally.shop.dao.ShippingDAO;
import com.sally.shop.dao.entity.ShippingEntity;
import com.sally.shop.dao.entity.ShippingItemEntity;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShippingServiceImpl implements ShippingService {

    private final ShippingDAO shippingDAO;
    private final EventGateway eventGateway;

    public ShippingServiceImpl(ShippingDAO shippingDAO, EventGateway eventGateway) {
        this.shippingDAO = shippingDAO;
        this.eventGateway = eventGateway;
    }

    @Override
    @Transactional
    public void createShipping(UUID shopId, UUID orderId, UUID customerId, String customerName, List<OrderItem> orderItems) {
        shippingDAO.saveShipping(shopId, orderId, customerId, customerName, orderItems);
    }

    @Override
    @Transactional
    public void deliverShipping(UUID shopId, UUID shippingId) {
        final ShippingEntity shippingEntity = shippingDAO.updateStatus(shopId, shippingId, ShippingStatus.DELIVERED);

        eventGateway.publish(new OrderShippedEvent(shippingEntity.getOrderId().toString()));
    }

    @Override
    @Transactional
    public void deniedShipping(UUID shopId, UUID shippingId) {
        final ShippingEntity shippingEntity = shippingDAO.updateStatus(shopId, shippingId, ShippingStatus.CANCELED);

        eventGateway.publish(new OrderShippingCanceledEvent(shippingEntity.getOrderId().toString()));
    }

    @Override
    @Transactional
    public List<Shipping> getAllShippingsForShop(UUID shopId) {
        return shippingDAO.getAllForShop(shopId)
                .stream()
                .map(this::mapShipping)
                .collect(Collectors.toList());
    }

    private Shipping mapShipping(ShippingEntity entity) {
        return Shipping.builder()
                .shippingId(entity.getId())
                .shopId(entity.getShopId())
                .orderId(entity.getOrderId())
                .customerId(entity.getCustomerId())
                .customerName(entity.getCustomerName())
                .shippingItems(shippingDAO.getItemsForShipping(entity.getId()).stream().map(this::mapShippingItem).collect(Collectors.toList()))
                .status(entity.getStatus())
                .build();
    }

    private ShippingItem mapShippingItem(ShippingItemEntity entity) {
        return ShippingItem.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .name(entity.getProductName())
                .price(entity.getPrice())
                .count(entity.getCount())
                .build();
    }
}
