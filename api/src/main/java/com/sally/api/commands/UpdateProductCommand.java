package com.sally.api.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductCommand {
    @TargetAggregateIdentifier
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
    private UUID shopId;
    private String shopName;
}
