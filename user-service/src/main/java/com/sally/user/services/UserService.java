package com.sally.user.services;

import com.sally.auth.UserRole;
import com.sally.user.dao.ShopDAO;
import com.sally.user.dao.UserDAO;
import com.sally.user.models.CustomerCreateRequest;
import com.sally.user.models.ShopOwnerCreateRequest;
import com.sally.user.models.User;
import com.sally.user.dao.entity.UserEntity;
import com.sally.utils.Sets;
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
