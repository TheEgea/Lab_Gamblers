package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Primary
public class JpaUserAuthAdapter implements UserAuthPort {

    private final UserJpaRepository repo;
    private final PasswordEncoder encoder;

    public JpaUserAuthAdapter(UserJpaRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> loadByUsername(Username username) {
        return repo.findByUsername(username.value())
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyPassword(Password password) {
        return encoder.matches(raw.value(), hashed.value());
    }

    private User toDomain(UserEntity e) {
        Set<Role> roles = e.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet());
        return new User(new UserId(e.getId()), new Username(e.getUsername()), new HashedPassword(e.getPasswordHash()), roles);
    }
}