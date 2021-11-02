package com.sally.order.dao;

import com.sally.api.OrderStatus;
import com.sally.domain.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Entity
@Builder
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderEntity extends AbstractEntity {
    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
