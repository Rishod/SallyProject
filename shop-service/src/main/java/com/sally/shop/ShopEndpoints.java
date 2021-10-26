package com.sally.shop;

public class ShopEndpoints {
    public static final String SERVICE_PREFIX = "/shop-service";
    public static final String V1 = SERVICE_PREFIX + "/api/v1";
    public static final String V2 = SERVICE_PREFIX + "/api/v2";
    public static final String PRODUCT = "/product";
    public static final String PRODUCT_BY_ID = PRODUCT + "/{id}";
    public static final String SHIPPING = "/shipping";
    public static final String DELIVER_SHIPPING = SHIPPING + "/deliver/{id}";
    public static final String CANCEL_SHIPPING = SHIPPING + "/cancel/{id}";
}
