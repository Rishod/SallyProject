package com.sally.shop.service;

import com.sally.api.Product;
import com.sally.api.requests.CreateProductRequest;
import com.sally.api.requests.UpdateProductRequest;
import com.sally.exceptions.ErrorCode;
import com.sally.exceptions.NotFoundException;
import com.sally.shop.dao.entity.ProductEntity;
import com.sally.shop.dao.ProductDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    @Transactional
    public Product saveProduct(final UUID shopId, final CreateProductRequest request) {
        final ProductEntity productEntity = productDAO.saveProduct(shopId, request.getName(), request.getDescription(),
                request.getPrice());

        return mapProduct(productEntity);
    }

    private Product mapProduct(final ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .shopId(productEntity.getShopId())
                .build();
    }

    @Override
    @Transactional
    public Product updateProduct(final UUID shopId, final UpdateProductRequest request) {
        final ProductEntity productEntity = productDAO.updateProduct(request.getId(), shopId, request.getName(),
                request.getDescription(), request.getPrice());

        return mapProduct(productEntity);
    }

    @Override
    @Transactional
    public Product getProduct(UUID productId) {
        return productDAO.getProductById(productId)
                .map(this::mapProduct)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND_BY_ID));
    }

    @Override
    @Transactional
    public List<Product> getProducts() {
        return productDAO.getAllProducts()
                .stream()
                .map(this::mapProduct)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProduct(UUID shopId, UUID productId) {
        productDAO.delete(shopId, productId);
    }
}
