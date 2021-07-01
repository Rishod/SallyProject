package com.sally.user.models;

import com.sally.domain.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Builder
@Table(name = "company")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShopEntity extends AbstractEntity {
    private String companyName;

    @JoinColumn(name = "company_owner_id")
    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity companyOwner;
}
