package com.sally.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/protected/api/authtest")
    public String protectedEndpoint(@AuthenticationPrincipal final SalyUserDetails principal) {
        return principal.getUsername();
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    @GetMapping(path = "/protected/api/authtest/customer")
    public String protectedCustomerEndpoint(@AuthenticationPrincipal final SalyUserDetails principal) {
        return principal.getUsername();
    }
}
