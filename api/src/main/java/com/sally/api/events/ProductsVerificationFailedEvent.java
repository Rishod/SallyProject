package com.sally.api.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsVerificationFailedEvent {
    private String orderId;
    private String reason;
}
