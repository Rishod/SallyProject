package com.sally.api.commands;

import static java.util.Optional.ofNullable;

import com.sally.api.OrderItem;
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
public class VerifyProductsCommand {
    private UUID orderId;
    private List<Product> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        private UUID productId;
        private int count;

        public static List<Product> ofOrderItems(List<OrderItem> items) {
            return ofNullable(items).orElseGet(ArrayList::new).stream().map(Product::ofOrderItem).collect(Collectors.toList());
        }

        public static Product ofOrderItem(OrderItem item) {
            return new Product(item.getProductId(), item.getCount());
        }

    }
}
