package com.sally.api.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteProductCommand {
    @TargetAggregateIdentifier
    private UUID productId;
    private UUID shopId;
}
