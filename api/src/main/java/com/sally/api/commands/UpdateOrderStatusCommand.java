package com.sally.api.commands;

import com.sally.api.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusCommand {
    @TargetAggregateIdentifier
    private UUID orderId;
    private OrderStatus orderStatus;
}
