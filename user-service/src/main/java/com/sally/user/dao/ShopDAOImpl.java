package com.sally.user.dao;

import com.sally.domain.CommonDAO;
import com.sally.user.dao.entity.ShopEntity;
import com.sally.user.dao.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;

@Repository
public class ShopDAOImpl extends CommonDAO implements ShopDAO {

    public ShopDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public ShopEntity createShop(String shopName, UserEntity ownerUserEntity) {
        ShopEntity shopEntity = ShopEntity.builder()
                .name(shopName)
                .shopOwner(ownerUserEntity)
                .build();

        entityManager.persist(shopEntity);

        return shopEntity;
    }

    @Override
    public Optional<ShopEntity> findByOwnerId(UUID userEntityId) {
        return findSingleResult(entityManager.createQuery("select shop from ShopEntity shop where shop.shopOwner.id = :ownerId", ShopEntity.class)
                .setParameter("ownerId", userEntityId));
    }
}
