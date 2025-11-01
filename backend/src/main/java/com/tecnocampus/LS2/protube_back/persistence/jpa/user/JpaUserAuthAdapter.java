package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// ... otros imports
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Primary
public class JpaUserAuthAdapter implements UserAuthPort {

    private final UserJpaRepository repo;
    private final PasswordEncoder encoder;
    private static final Logger log = LoggerFactory.getLogger(JpaUserAuthAdapter.class);

    public JpaUserAuthAdapter(UserJpaRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }
    @Override
    public void save(User user) {

        repo.save(UserMapper.toEntity(user));
    }

    public void debugShowAllUsers() {
        log.info("🗃️ USUARIOS EN DB:");
        repo.findAll().forEach(user -> {
            log.info("   - Username: {}, Hash: {}, Role: {}",
                    user.getUsername(),
                    user.getPasswordHash().substring(0, 15) + "...",
                    user.getRole());
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> loadByUsername(Username username) {
        log.info("🔍 BUSCANDO usuario: {}", username.value());

        Optional<UserEntity> userEntity = repo.findByUsername(username.value());

        if (userEntity.isPresent()) {
            log.info("✅ Usuario ENCONTRADO: {} con hash: {}",
                    userEntity.get().getUsername(),
                    userEntity.get().getPasswordHash().substring(0, 10) + "...");
        } else {
            log.warn("❌ Usuario NO ENCONTRADO: {}", username.value());
        }

        return userEntity.map(this::toDomain);
    }

    // Añade este método para debuggar login
    public boolean login(Username username, String rawPassword) {
        log.info("🔐 LOGIN ATTEMPT - Usuario: {}, Password raw: {}", username.value(), rawPassword);

        Optional<UserEntity> userEntity = repo.findByUsername(username.value());

        if (userEntity.isEmpty()) {
            log.warn("❌ LOGIN FAILED - Usuario no existe: {}", username.value());
            return false;
        }

        UserEntity user = userEntity.get();
        String storedHash = user.getPasswordHash();

        log.info("🔑 COMPARING - Raw: '{}' vs StoredHash: '{}'", rawPassword, storedHash);
        log.info("🔧 PasswordEncoder type: {}", encoder.getClass().getSimpleName());

        boolean matches = encoder.matches(rawPassword, storedHash);
        log.info("🎯 PASSWORD MATCH RESULT: {}", matches);

        return matches;
    }

    @Transactional(readOnly = true)
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        log.info("🔍 VERIFY PASSWORD - Raw: '{}' vs Hash: '{}'", rawPassword, hashedPassword);
        boolean result = encoder.matches(rawPassword, hashedPassword);
        log.info("✅ VERIFY RESULT: {}", result);
        return result;
    }

    private User toDomain(UserEntity e) {
        log.info("🔄 MAPPING UserEntity to Domain - Username: {}, Role: {}", e.getUsername(), e.getRole());

        try {
            Role role = e.getRole(); // ❌ NO uses .name() aquí
            User user = new User(new UserId(e.getId()), new Username(e.getUsername()), new Password(e.getPasswordHash()), role);
            log.info("✅ MAPPING SUCCESS - Domain user created");
            return user;
        } catch (Exception ex) {
            log.error("❌ MAPPING ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

/*
    @Override
    @Transactional(readOnly = true)
    public Optional<User> loadByUsername(Username username) {
        return repo.findByUsername(username.value())
                .map(this::toDomain);
    }

    @Override
    public void save(User user) {

        repo.save(UserMapper.toEntity(user));

    }


    @Transactional(readOnly = true)
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }

    private User toDomain(UserEntity e) {
        Role role = Role.valueOf(e.getRole().name());
        return new User(new UserId(e.getId()), new Username(e.getUsername()), new Password(e.getPasswordHash()),role);
    }

 */

}
