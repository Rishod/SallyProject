package com.sally.api;

import static java.util.Optional.ofNullable;

import com.sally.api.commands.CreateOrderCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private UUID id;
    private UUID productId;
    private String productName;
    private int count;

    public static List<OrderItem> ofCommandItem(List<CreateOrderCommand.Item> items) {
        return ofNullable(items).orElseGet(ArrayList::new).stream().map(OrderItem::ofCommandItem).collect(Collectors.toList());
    }

    public static OrderItem ofCommandItem(CreateOrderCommand.Item item) {
        return new OrderItem(UUID.randomUUID(), item.getProductId(), null, item.getCount());
    }
}
