package com.sally.shop.controller;

import static com.sally.shop.models.ShopEndpoints.PRODUCT;
import static com.sally.shop.models.ShopEndpoints.PRODUCT_BY_ID;
import static com.sally.shop.models.ShopEndpoints.V1;

import com.sally.auth.SalyUserDetails;
import com.sally.shop.models.CreateProductRequest;
import com.sally.shop.models.Product;
import com.sally.shop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
    public Product createProduct(@RequestBody CreateProductRequest request,
                                 @AuthenticationPrincipal final SalyUserDetails userDetails) {
        final UUID shopId = userDetails.getShopDetails().getId();
        return productService.saveProduct(shopId, request);
    }

    @PutMapping(PRODUCT)
    @PreAuthorize("hasRole('ROLE_SHOP_OWNER')")
    public Product editProduct(@RequestBody UpdateProductRequest request, @AuthenticationPrincipal SalyUserDetails userDetails) {
        final UUID shopId = userDetails.getShopDetails().getId();
        return productService.updateProduct(shopId, request);
    }

    @GetMapping(PRODUCT_BY_ID)
    public Product getById(@PathVariable(name = "id") UUID productId) {
        return productService.getProduct(productId);
    }
}
