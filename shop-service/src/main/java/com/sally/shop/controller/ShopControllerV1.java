package com.sally.shop.controller;

import static com.sally.shop.ShopEndpoints.PRODUCT;
import static com.sally.shop.ShopEndpoints.PRODUCT_BY_ID;
import static com.sally.shop.ShopEndpoints.V1;

import com.sally.api.Product;
import com.sally.api.requests.CreateProductRequest;
import com.sally.api.requests.UpdateProductRequest;
import com.sally.auth.SalyUserDetails;
import com.sally.shop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(V1)
public class ShopControllerV1 {

    private ProductService productService;

    public ShopControllerV1(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(PRODUCT)
    @PreAuthorize("hasRole('ROLE_SHOP_OWNER')")
    public CompletableFuture<Product> createProduct(@RequestBody CreateProductRequest request,
                                                    @AuthenticationPrincipal final SalyUserDetails userDetails) {
        return productService.saveProduct(userDetails.getShopDetails(), request);
    }

    @PutMapping(PRODUCT)
    @PreAuthorize("hasRole('ROLE_SHOP_OWNER')")
    public CompletableFuture<Product> editProduct(@RequestBody UpdateProductRequest request, @AuthenticationPrincipal SalyUserDetails userDetails) {
        return productService.updateProduct(userDetails.getShopDetails(), request);
    }

    @DeleteMapping(PRODUCT_BY_ID)
    @PreAuthorize("hasRole('ROLE_SHOP_OWNER')")
    public void deleteProduct(@PathVariable(name = "id") UUID productId, @AuthenticationPrincipal SalyUserDetails userDetails) {
        final UUID shopId = userDetails.getShopDetails().getId();
        productService.deleteProduct(shopId, productId);
    }

    @GetMapping(PRODUCT_BY_ID)
    public Product getById(@PathVariable(name = "id") UUID productId) {
        return productService.getProduct(productId);
    }

    @GetMapping(PRODUCT)
    public List<Product> getListProducts() {
        return productService.getProducts();
    }
}
