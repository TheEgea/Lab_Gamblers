package com.tecnocampus.LS2.protube_back.infrastructure.auth;

import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import com.tecnocampus.LS2.protube_back.infrastructure.persistence.UserEntity;
import com.tecnocampus.LS2.protube_back.infrastructure.persistence.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthAdapter implements UserAuthPort {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAuthAdapter(UserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> loadByUsername(Username username) {
        return userRepository.findByUsername(username.value())
                .map(this::toDomain);
    }

    @Override
    public boolean verifyPassword(RawPassword raw, HashedPassword hashed) {
        return passwordEncoder.matches(raw.value(), hashed.value());
    }

    private User toDomain(UserEntity entity) {
        return new User(
                new UserId(entity.getId()),
                new Username(entity.getUsername()),
                new HashedPassword(entity.getPasswordHash()),
                entity.getRoles()
        );
    }
}
