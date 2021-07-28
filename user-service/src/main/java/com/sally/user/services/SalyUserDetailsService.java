package com.sally.user.services;

import com.sally.auth.SalyUserDetails;
import com.sally.auth.SalyUserDetailsWithPassword;

import com.sally.auth.ShopDetails;
import com.sally.exceptions.ErrorCode;
import com.sally.exceptions.NotFoundException;
import com.sally.user.dao.ShopDAO;
import com.sally.user.dao.UserDAO;
import com.sally.user.dao.entity.ShopEntity;
import com.sally.user.dao.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalyUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;
    private final ShopDAO shopDAO;

    public SalyUserDetailsService(UserDAO userDAO, ShopDAO shopDAO) {
        this.userDAO = userDAO;
        this.shopDAO = shopDAO;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDAO.findByUsername(email)
                .map(this::mapUserDetails)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL));
    }

    @Transactional
    public SalyUserDetails loadUserByUsernameWithoutPassword(String email) throws UsernameNotFoundException {
        return userDAO.findByUsername(email)
                .map(this::mapUserDetailsWithoutPassword)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL));
    }

    private UserDetails mapUserDetails(UserEntity userEntity) {
        return new SalyUserDetailsWithPassword(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getRoles(),
                userEntity.getPassword()
        );
    }

    private SalyUserDetails mapUserDetailsWithoutPassword(final UserEntity userEntity) {
        return SalyUserDetails.builder()
                .userId(userEntity.getId())
                .username(userEntity.getUsername())
                .roles(userEntity.getRoles())
                .shopDetails(shopDAO.findByOwnerId(userEntity.getId()).map(this::mapShopDetails).orElse(null))
                .build();
    }

    private ShopDetails mapShopDetails(ShopEntity shopEntity) {
        return new ShopDetails(shopEntity.getId(), shopEntity.getName());
    }

}
