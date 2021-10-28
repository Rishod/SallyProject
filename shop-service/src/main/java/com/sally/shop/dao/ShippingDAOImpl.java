package com.sally.shop.dao;

import com.sally.api.OrderItem;
import com.sally.api.ShippingStatus;
import com.sally.domain.CommonDAO;
import com.sally.exceptions.ErrorCode;
import com.sally.exceptions.NotFoundException;
import com.sally.shop.dao.entity.ShippingEntity;
import com.sally.shop.dao.entity.ShippingItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;

@Repository
public class ShippingDAOImpl extends CommonDAO implements ShippingDAO {

    protected ShippingDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void saveShipping(UUID shopId, UUID orderId, UUID customerId, String customerName, List<OrderItem> orderItems) {
        final ShippingEntity shippingEntity = ShippingEntity.builder()
                .shopId(shopId)
                .orderId(orderId)
                .customerId(customerId)
                .customerName(customerName)
                .status(ShippingStatus.NEW)
                .build();

        this.entityManager.persist(shippingEntity);

        persistItems(shippingEntity, orderItems);
    }

    @Override
    public ShippingEntity updateStatus(final UUID shopId, final UUID shippingId, ShippingStatus status) {
        final ShippingEntity shippingEntity = findByIdAndShop(shopId, shippingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND_BY_ID));

        shippingEntity.setStatus(status);
        this.entityManager.merge(shippingEntity);

        return shippingEntity;
    }

    @Override
    public List<ShippingEntity> getAllForShop(UUID shopId) {
        return entityManager.createQuery("select s from ShippingEntity s where s.shopId = :shopId", ShippingEntity.class)
                .setParameter("shopId", shopId)
                .getResultList();
    }

    @Override
    public List<ShippingItemEntity> getItemsForShipping(UUID shippingId) {
        return entityManager.createQuery("select i from ShippingItemEntity i where i.shippingEntity.id = :shippingId", ShippingItemEntity.class)
                .setParameter("shippingId", shippingId)
                .getResultList();
    }

    private Optional<ShippingEntity> findByIdAndShop(final UUID shopId, final UUID shippingId) {
        return findSingleResult(entityManager.createQuery("select s from ShippingEntity s where s.id = :shippingId and s.shopId = :shopId", ShippingEntity.class)
                .setParameter("shopId", shopId)
                .setParameter("shippingId", shippingId));
    }

    private void persistItems(ShippingEntity shippingEntity, List<OrderItem> orderItems) {
        orderItems.stream()
                .map(item -> buildShippingItemEntity(shippingEntity, item))
                .forEach(this.entityManager::persist);
    }

    private ShippingItemEntity buildShippingItemEntity(final ShippingEntity shippingEntity, final OrderItem item) {
        return ShippingItemEntity.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .count(item.getCount())
                .price(item.getPrice())
                .shippingEntity(shippingEntity)
                .build();
    }
}
