package com.sally.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {
    private UUID shippingId;
    private UUID shopId;
    private UUID orderId;
    private UUID customerId;
    private String customerName;
    private List<ShippingItem> shippingItems;
    private ShippingStatus status;
}
