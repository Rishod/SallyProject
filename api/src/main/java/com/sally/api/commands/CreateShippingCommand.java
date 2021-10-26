package com.sally.api.commands;

import com.sally.api.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateShippingCommand {
    private UUID orderId;
    private UUID customerId;
    private UUID shopId;
    private String customerName;
    private List<OrderItem> orderItems;
}
