package com.sally.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private UUID id;
    private UUID customerId;
    private List<OrderItem> orderItems;
    private BigDecimal total;
    private OrderStatus status;
}
