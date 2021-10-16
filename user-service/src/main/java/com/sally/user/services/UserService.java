package com.sally.user.services;

import com.sally.api.Shop;
import com.sally.api.User;
import com.sally.api.UserRole;
import com.sally.api.requests.CustomerCreateRequest;
import com.sally.api.requests.ShopOwnerCreateRequest;
import com.sally.user.dao.ShopDAO;
import com.sally.user.dao.UserDAO;
import com.sally.user.dao.entity.ShopEntity;
import com.sally.user.dao.entity.UserEntity;
import com.sally.utils.Sets;
import io.jsonwebtoken.lang.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserDAO userDAO;
    private ShopDAO shopDAO;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO, ShopDAO shopDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.shopDAO = shopDAO;
    }

    @Transactional
    public User createCustomer(final CustomerCreateRequest request) {
        final UserEntity userEntity = userDAO.create(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Sets.of(UserRole.CUSTOMER)
        );

        return mapUser(userEntity);
    }

    private User mapUser(UserEntity userEntity) {
        return User.builder()
                .username(userEntity.getUsername())
                .roles(userEntity.getRoles())
                .shop(shopDAO.findByOwnerId(userEntity.getId()).map(this::mapShop).orElse(null))
                .build();
    }

    private Shop mapShop(ShopEntity shopEntity) {
        return Shop.builder()
                .id(shopEntity.getId())
                .name(shopEntity.getName())
                .build();
    }

    @Transactional
    public User getUserByUsername(String username) {
        final UserEntity userEntity = userDAO.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Unexpected behavior. User Should be exist"));

        return mapUser(userEntity);
    }

    @Transactional
    public User createShopOwner(final ShopOwnerCreateRequest request) {
        final UserEntity userEntity = userDAO.create(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Sets.of(UserRole.SHOP_OWNER)
        );

        shopDAO.createShop(request.getCompanyName(), userEntity);

        return mapUser(userEntity);
    }
}
