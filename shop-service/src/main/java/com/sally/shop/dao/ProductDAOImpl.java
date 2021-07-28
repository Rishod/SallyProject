package com.sally.shop.dao;

import com.sally.domain.CommonDAO;
import com.sally.exceptions.ErrorCode;
import com.sally.exceptions.NotFoundException;
import com.sally.shop.dao.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;

@Repository
public class ProductDAOImpl extends CommonDAO implements ProductDAO {

    protected ProductDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public ProductEntity saveProduct(UUID shopId, String name, String description, BigDecimal price) {
        final ProductEntity entity = ProductEntity.builder()
                .name(name)
                .description(description)
                .price(price)
                .shopId(shopId)
                .build();

        entityManager.persist(entity);
        return entity;
    }

    public Optional<ProductEntity> findProductByIdAndShop(UUID id, UUID shopId) {
        return findSingleResult(entityManager.createQuery("select p from ProductEntity p where p.id = :id" +
                " and p.shopId = :shopId", ProductEntity.class)
                .setParameter("id", id)
                .setParameter("shopId", shopId));
    }

    @Override
    public ProductEntity updateProduct(UUID productId, UUID shopId, String name, String description, BigDecimal price) {
        final ProductEntity productEntity = findProductByIdAndShop(productId, shopId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND_BY_ID));

        productEntity.setName(name);
        productEntity.setDescription(description);
        productEntity.setPrice(price);

        entityManager.merge(productEntity);

        return productEntity;
    }

    @Override
    public Optional<ProductEntity> getProductById(UUID productId) {
        return findSingleResult(entityManager.createQuery("select p from ProductEntity p where p.id = :id", ProductEntity.class)
                .setParameter("id", productId));
    }
}
