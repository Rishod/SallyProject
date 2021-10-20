package com.sally.api.events;

import com.sally.api.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsUpdatedEvent {
    private UUID id;
    private UUID customerId;
    private List<OrderItem> orderItems;
    private BigDecimal total;
}
