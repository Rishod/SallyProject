package com.sally.shop.service;

import com.sally.api.Product;
import com.sally.api.requests.CreateProductRequest;
import com.sally.api.requests.UpdateProductRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product saveProduct(UUID shopId, CreateProductRequest request);

    Product updateProduct(UUID shopId, UpdateProductRequest request);

    Product getProduct(UUID productId);

    List<Product> getProducts();

    void deleteProduct(UUID shopId, UUID productId);
}
