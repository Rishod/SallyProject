package com.sally.order.dao;

import com.sally.api.OrderItem;
import com.sally.api.OrderStatus;
import com.sally.domain.CommonDAO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;

@Repository
public class OrderDAOImpl extends CommonDAO implements OrderDAO {

    public OrderDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<OrderEntity> getAllForCustomerId(UUID userId) {
        return this.entityManager.createQuery("select o from OrderEntity o where o.customerId = :id", OrderEntity.class)
                .setParameter("id", userId)
                .getResultList();
    }

    @Override
    public List<OrderItemEntity> getOrderItems(UUID orderId) {
        return this.entityManager.createQuery("select i from OrderItemEntity i where i.orderEntity.id = :orderId", OrderItemEntity.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public void create(UUID orderId, UUID customerId, List<OrderItem> items, OrderStatus status) {
        OrderEntity order = OrderEntity.builder()
                .customerId(customerId)
                .status(status)
                .total(new BigDecimal("0.00"))
                .build();

        order.setId(orderId);
        entityManager.persist(order);

        saveItems(order, items);
    }

    @Override
    public void updateOrder(UUID id, BigDecimal total, List<OrderItem> items) {
        getOrderById(id).ifPresent(orderEntity -> {
            orderEntity.setTotal(total);
            orderEntity.setUpdatedAt(LocalDateTime.now());

            entityManager.merge(orderEntity);

            updateItems(orderEntity, items);
        });
    }

    @Override
    public void updateStatus(UUID orderId, OrderStatus orderStatus) {
        getOrderById(orderId).ifPresent(orderEntity -> {
            orderEntity.setStatus(orderStatus);
            orderEntity.setUpdatedAt(LocalDateTime.now());

            entityManager.merge(orderEntity);
        });
    }

    private Optional<OrderEntity> getOrderById(UUID orderId) {
        return findSingleResult(entityManager.createQuery("select o from OrderEntity o where o.id = :id", OrderEntity.class)
                .setParameter("id", orderId));
    }

    private void saveItems(OrderEntity order, List<OrderItem> items) {
        items.stream()
                .map(item -> buildOrderItemEntity(order, item))
                .forEach(this.entityManager::persist);
    }

    private void updateItems(OrderEntity order, List<OrderItem> items) {
        items.stream()
                .map(item -> buildOrderItemEntity(order, item))
                .forEach(this.entityManager::merge);
    }

    private OrderItemEntity buildOrderItemEntity(OrderEntity order, OrderItem item) {
        return OrderItemEntity.builder()
                .order(order)
                .productId(item.getProductId())
                .count(item.getCount())
                .productName(item.getProductName())
                .price(item.getPrice())
                .build();
    }
}
