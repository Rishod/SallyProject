package com.sally.shop.dao;

import com.sally.shop.dao.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductDAO {

    ProductEntity saveProduct(UUID shopId, String name, String description, BigDecimal price);

    ProductEntity updateProduct(UUID productId, UUID shopId, String name, String description, BigDecimal price);

    Optional<ProductEntity> getProductById(UUID productId);

    List<ProductEntity> getAllProducts();

    void delete(UUID shopId, UUID productId);
}
