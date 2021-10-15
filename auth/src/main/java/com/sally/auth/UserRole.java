package com.sally.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum UserRole {
    SUPER_USER(Roles.SUPER_USER),
    CUSTOMER(Roles.CUSTOMER),
    SHOP_OWNER(Roles.SHOP_OWNER);

    private final String role;

    public GrantedAuthority asAuthority() {
        return new AuthorityImpl(this);
    }

}
