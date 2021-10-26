package com.sally.api.events;

import com.sally.api.OrderItem;
import com.sally.api.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private String orderId;
    private String customerId;
    private String customerName;
    private List<OrderItem> orderItems;
    private OrderStatus status;
}
