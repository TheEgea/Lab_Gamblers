package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

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
        // Crear usuario admin si no existe
        if (repo.findByUsername("admin").isEmpty()) {
            UserEntity admin = new UserEntity(
                    UUID.randomUUID(),
                    "admin",
                    encoder.encode("admin123"), // BCrypt hash
                    Set.of("ADMIN", "USER")
            );
            repo.save(admin);
            System.out.println("✅ Usuario admin creado con contraseña: admin123");
        } else {
            System.out.println("👤 Usuario admin ya existe");
        }

        // Crear usuario regular si no existe
        if (repo.findByUsername("user").isEmpty()) {
            UserEntity user = new UserEntity(
                    UUID.randomUUID(),
                    "user",
                    encoder.encode("user123"),
                    Set.of("USER")
            );
            repo.save(user);
            System.out.println("✅ Usuario user creado con contraseña: user123");
        } else {
            System.out.println("👤 Usuario user ya existe");
        }
    }
}
