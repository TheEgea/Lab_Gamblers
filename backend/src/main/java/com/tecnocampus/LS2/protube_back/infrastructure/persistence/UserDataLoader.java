package com.tecnocampus.LS2.protube_back.infrastructure.persistence;

import com.tecnocampus.LS2.protube_back.domain.user.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@Profile("dev")
public class UserDataLoader implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataLoader.class);

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    public UserDataLoader(UserJpaRepository userRepository, PasswordEncoder passwordEncoder, Environment env) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) {
        Boolean loadInitialData = env.getProperty("pro_tube.load_initial_data", Boolean.class);
        if (Boolean.TRUE.equals(loadInitialData)) {
            loadSeedUsers();
        }
    }

    private void loadSeedUsers() {
        if (userRepository.count() == 0) {
            LOG.info("Loading seed users...");

            // Admin user
            UserEntity admin = new UserEntity(
                    UUID.randomUUID(),
                    "admin",
                    passwordEncoder.encode("admin123"),
                    Set.of(Role.ADMIN, Role.USER)
            );
            userRepository.save(admin);

            // Regular user
            UserEntity user = new UserEntity(
                    UUID.randomUUID(),
                    "user",
                    passwordEncoder.encode("user123"),
                    Set.of(Role.USER)
            );
            userRepository.save(user);

            LOG.info("Seed users loaded: admin/admin123, user/user123");
        } else {
            LOG.info("Users already exist, skipping seed data");
        }
    }
}
