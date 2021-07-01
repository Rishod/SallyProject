package com.sally.shop.controller;

import com.sally.auth.SalyUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopControllerV1 {

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SHOP_OWNER')")
    public Product createProduct(@RequestBody CreateProductRequest request,
                                 @AuthenticationPrincipal final SalyUserDetails userDetails) {
        return null;
    }
}
