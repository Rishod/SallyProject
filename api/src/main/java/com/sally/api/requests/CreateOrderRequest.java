package com.sally.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private List<Item> orderItems;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private UUID productId;
        private int count;
    }
}
