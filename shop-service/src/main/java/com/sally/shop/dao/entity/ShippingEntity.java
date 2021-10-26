package com.sally.shop.dao.entity;

import com.sally.api.ShippingStatus;
import com.sally.domain.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Entity
@Builder
@Table(name = "shipping")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShippingEntity extends AbstractEntity {

    @Column(name = "shop_id")
    private UUID shopId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ShippingStatus status = ShippingStatus.NEW;
}
