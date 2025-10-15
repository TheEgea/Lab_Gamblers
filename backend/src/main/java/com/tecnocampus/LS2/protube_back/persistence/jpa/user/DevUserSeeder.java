package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

// Ajusta aquí cualquier import que apunte a 'infrastructure.persistence.jpa.user' → 'persistence.jpa.user'
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.UUID;

@Component
@Profile("dev")
public class DevUserSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DevUserSeeder.class);

    private final UserJpaRepository repo;
    private final PasswordEncoder encoder;

    public DevUserSeeder(UserJpaRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repo.findByUsername("admin").isPresent()) {
            log.info("Dev user 'admin' already exists. Skipping seeding.");
            return;
        }
        var user = new UserEntity(
                UUID.randomUUID(),
                "admin",
                encoder.encode("admin123"),
                Set.of("ADMIN")
        );
        repo.save(user);
        log.info("Dev user 'admin' created with default password.");
    }
}