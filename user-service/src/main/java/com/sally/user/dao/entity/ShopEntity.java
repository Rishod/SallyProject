package com.sally.user.dao.entity;

import com.sally.domain.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Builder
@Table(name = "shop")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShopEntity extends AbstractEntity {
    @Column(name = "name")
    private String name;

    @JoinColumn(name = "shop_owner_id")
    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity shopOwner;
}
