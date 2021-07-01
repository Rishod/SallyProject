package com.sally.auth;

import org.springframework.security.core.Authentication;

public class SallyAuthenticationHelper {
    public static SalyUserDetails fetchUserDetails(Authentication authentication) {
        return (SalyUserDetails) authentication.getPrincipal();
    }
}
