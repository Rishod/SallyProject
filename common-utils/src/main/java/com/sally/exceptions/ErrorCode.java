package com.sally.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_ERROR("Internal server error"),
    BAD_CREDENTIALS("Invalid username or password"),
    ACCESS_DENIED("Access denied for this user"),
    UNSUPPORTED_METHOD("HTTP method not supported for this endpoint"),
    BAD_REQUEST_BODY("Bad request body"),
    USER_NOT_FOUND_BY_EMAIL("User not found with entered email"),
    PRODUCT_NOT_FOUND_BY_ID("Product not found with entered id"),
    SHIPPING_NOT_FOUND_BY_ID("Sipping not found with entered id");

    private String message;
}
