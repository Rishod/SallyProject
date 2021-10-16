package com.sally.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    SUPER_USER(Roles.SUPER_USER),
    CUSTOMER(Roles.CUSTOMER),
    SHOP_OWNER(Roles.SHOP_OWNER);

    private final String role;

}
