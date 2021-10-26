package com.sally.api.commands;

import static java.util.Optional.ofNullable;

import com.sally.api.requests.CreateOrderRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    private UUID orderId;
    private UUID customerId;
    private String customerName;
    private List<Item> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private UUID productId;
        private int count;

        public static List<Item> fromRequest(List<CreateOrderRequest.Item> items) {
            return ofNullable(items).orElseGet(ArrayList::new).stream().map(Item::fromRequest).collect(Collectors.toList());
        }

        public static Item fromRequest(CreateOrderRequest.Item item) {
            return new Item(item.getProductId(), item.getCount());
        }
    }
}
