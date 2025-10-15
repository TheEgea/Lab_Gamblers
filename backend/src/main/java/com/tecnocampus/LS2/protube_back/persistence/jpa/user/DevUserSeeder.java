package com.tecnocampus.LS2.protube_back.infrastructure.persistence.jpa.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@Profile("dev")
public class DevUserSeeder implements CommandLineRunner {

    private final UserJpaRepository repo;
    private final PasswordEncoder encoder;

    public DevUserSeeder(UserJpaRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        repo.findByUsername("admin").ifPresent(u -> { /* ya existe */ throw new RuntimeException("Dev user already exists"); });
        var user = new UserEntity(
                UUID.randomUUID(),
                "admin",
                encoder.encode("admin123"),
                Set.of("ADMIN")
        );
        try {
            repo.save(user);
        } catch (Exception ignored) {
            // si la tabla ya existía y el usuario también, ignoramos
        }
    }
}