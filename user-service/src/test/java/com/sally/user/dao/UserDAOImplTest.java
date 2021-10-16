package com.sally.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.sally.api.UserRole;
import com.sally.user.DaoTest;
import com.sally.user.dao.entity.UserEntity;
import com.sally.utils.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserDAOImplTest extends DaoTest {

    @Autowired
    private UserDAOImpl userDAO;

    @Test
    void testCreateUser() {
        UserEntity userEntity = userDAO.create("name", "password", Sets.of(UserRole.CUSTOMER));
        assertThat(userEntity.getId()).isNotNull();
    }
}