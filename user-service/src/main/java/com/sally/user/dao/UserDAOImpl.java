package com.sally.user.dao;

import com.sally.auth.UserRole;
import com.sally.domain.CommonDAO;
import com.sally.user.dao.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;

@Repository
public class UserDAOImpl extends CommonDAO implements UserDAO {

    public UserDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public UserEntity create(String username, String encodedPassword, Set<UserRole> roles) {
        final UserEntity userEntity = new UserEntity(username, encodedPassword, roles);

        entityManager.persist(userEntity);

        return userEntity;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return findSingleResult(entityManager.createQuery("select u from UserEntity u where u.username = :username", UserEntity.class)
                .setParameter("username", username));
    }
}
