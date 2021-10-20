package com.sally.api.events;

import com.sally.api.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsVerificationSuccessEvent {
    private String orderId;
    private List<OrderItem> verifiedProducts;
    private BigDecimal total;
}
