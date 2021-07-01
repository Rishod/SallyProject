package com.sally.user.dao;

import com.sally.user.models.ShopEntity;
import com.sally.user.models.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface ShopDAO {
    ShopEntity createShop(String shopName, UserEntity userEntity);

    Optional<ShopEntity> findByOwnerId(UUID userEntityId);
}
