package com.sally.api;

import java.util.List;
import java.util.UUID;

public class Shipping {
    private UUID orderId;
    private List<ShippingItem> shippingItems;
    private ShippingStatus status;
}
