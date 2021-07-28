package com.sally.shop.service;

import com.sally.exceptions.ErrorCode;
import com.sally.exceptions.NotFoundException;
import com.sally.shop.controller.UpdateProductRequest;
import com.sally.shop.dao.entity.ProductEntity;
import com.sally.shop.dao.ProductDAO;
import com.sally.shop.models.CreateProductRequest;
import com.sally.shop.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Transactional
    public Product saveProduct(final UUID shopId, final CreateProductRequest request) {
        final ProductEntity productEntity = productDAO.saveProduct(shopId, request.getName(), request.getDescription(),
                request.getPrice());

        return mapProduct(productEntity);
    }

    private Product mapProduct(final ProductEntity productEntity) {
        return Product.builder()
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .build();
    }

    @Transactional
    public Product updateProduct(final UUID shopId, final UpdateProductRequest request) {
        final ProductEntity productEntity = productDAO.updateProduct(request.getId(), shopId, request.getName(),
                request.getDescription(), request.getPrice());

        return mapProduct(productEntity);
    }

    @Transactional
    public Product getProduct(UUID productId) {
        return productDAO.getProductById(productId)
                .map(this::mapProduct)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND_BY_ID));
    }
}
