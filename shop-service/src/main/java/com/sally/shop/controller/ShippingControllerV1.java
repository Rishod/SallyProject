package com.sally.shop.controller;

import static com.sally.shop.ShopEndpoints.CANCEL_SHIPPING;
import static com.sally.shop.ShopEndpoints.DELIVER_SHIPPING;
import static com.sally.shop.ShopEndpoints.SHIPPING;
import static com.sally.shop.ShopEndpoints.V1;

import com.sally.api.Shipping;
import com.sally.auth.SalyUserDetails;
import com.sally.shop.service.ShippingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping(V1)
@RestController
public class ShippingControllerV1 {

    private final ShippingService shippingService;

    public ShippingControllerV1(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping(SHIPPING)
    @PreAuthorize("hasRole('ROLE_SHOP_OWNER')")
    public List<Shipping> getShopShippings(@AuthenticationPrincipal SalyUserDetails userDetails) {
        return shippingService.getAllShippingsForShop(userDetails.getShopDetails().getId());
    }

    @PostMapping(DELIVER_SHIPPING)
    @PreAuthorize("hasRole('ROLE_SHOP_OWNER')")
    public void deliverShipping(@AuthenticationPrincipal SalyUserDetails userDetails, @PathVariable("id") UUID shippingId) {
        shippingService.deliverShipping(userDetails.getShopDetails().getId(), shippingId);
    }

    @PostMapping(CANCEL_SHIPPING)
    @PreAuthorize("hasRole('ROLE_SHOP_OWNER')")
    public void cancelShipping(@AuthenticationPrincipal SalyUserDetails userDetails, @PathVariable("id") UUID shippingId) {
        shippingService.deniedShipping(userDetails.getShopDetails().getId(), shippingId);
    }
}
