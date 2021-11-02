package com.sally.shop.service;

import com.sally.api.Product;
import com.sally.api.requests.CreateProductRequest;
import com.sally.api.requests.UpdateProductRequest;
import com.sally.auth.ShopDetails;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ProductService {
    CompletableFuture<Product> saveProduct(ShopDetails shop, CreateProductRequest request);

    CompletableFuture<Product> updateProduct(ShopDetails shop, UpdateProductRequest request);

    Product getProduct(UUID productId);

    List<Product> getProducts();

    CompletableFuture<Product> queryProduct(UUID productId);

    CompletableFuture<List<Product>> queryProducts();

    void deleteProduct(UUID shopId, UUID productId);
}
