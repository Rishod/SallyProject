package com.sally.api.commands;

import com.sally.api.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderItemsCommand {
    @TargetAggregateIdentifier
    private UUID orderId;
    private List<OrderItem> items;
    private BigDecimal total;
}
