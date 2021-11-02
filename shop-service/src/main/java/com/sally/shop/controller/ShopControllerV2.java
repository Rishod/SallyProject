package com.sally.shop.controller;

import static com.sally.shop.ShopEndpoints.PRODUCT;
import static com.sally.shop.ShopEndpoints.PRODUCT_BY_ID;
import static com.sally.shop.ShopEndpoints.V2;

import com.sally.api.Product;
import com.sally.shop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(V2)
public class ShopControllerV2 {
    private final ProductService productService;

    public ShopControllerV2(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(PRODUCT_BY_ID)
    public CompletableFuture<Product> getById(@PathVariable(name = "id") UUID productId) {
        return productService.queryProduct(productId);
    }

    @GetMapping(PRODUCT)
    public CompletableFuture<List<Product>> getListProducts() {
        return productService.queryProducts();
    }
}
