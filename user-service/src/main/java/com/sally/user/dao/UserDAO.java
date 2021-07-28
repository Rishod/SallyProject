package com.sally.user.dao;

import com.sally.auth.UserRole;
import com.sally.user.dao.entity.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface UserDAO {
    UserEntity create(String username, String encodedPassword, Set<UserRole> roles);

    Optional<UserEntity> findByUsername(String username);
}
