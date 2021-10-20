package com.sally.api.events;

import com.sally.api.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdatedEvent {
    private UUID orderId;
    private OrderStatus orderStatus;
}
