package com.sally.user;

import com.sally.api.UserRole;
import com.sally.user.dao.UserDAO;
import com.sally.user.dao.entity.UserEntity;
import com.sally.utils.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class SuperUserInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final String superUserName;
    private final String superUserPassword;
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;

    public SuperUserInitializer(@Value("${sally.super-user.name}") String superUserName,
                                @Value("${sally.super-user.password}") String superUserPassword,
                                PasswordEncoder passwordEncoder, UserDAO userDAO) {
        this.superUserName = superUserName;
        this.superUserPassword = superUserPassword;
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        final UserEntity superUser = userDAO.findByUsername(superUserName)
                .orElseGet(() -> {
                    log.info("Creating Super User: {}", superUserName);
                    return userDAO.create(superUserName, passwordEncoder.encode(superUserPassword), Sets.of(UserRole.SUPER_USER));
                });

        log.info("Sally Super User (id: {}, name: {})", superUser.getId(), superUser.getUsername());
    }
}
