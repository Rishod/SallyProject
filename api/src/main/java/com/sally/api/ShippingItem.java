package com.sally.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingItem {
    private UUID id;
    private UUID productId;
    private String name;
    private int count;
    private BigDecimal price;
}
