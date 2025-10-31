package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JpaUserAuthAdapter implements UserAuthPort {

    private final UserJpaRepository userRepository;

    public JpaUserAuthAdapter(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> loadByUsername(Username username) {
        return userRepository.findByUsername(username.value())
                .map(this::toDomain);
    }

    @Override
    public void save(User user) {
        UserEntity entity = fromDomain(user);
        userRepository.save(entity);
    }

    private User toDomain(UserEntity entity) {
        Set<Role> roles = entity.getRoles().stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        return new User(
                new UserId(entity.getId()),
                new Username(entity.getUsername()),
                new Password(entity.getPasswordHash()), // Ya está hasheada
                roles
        );
    }

    private UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id().value());
        entity.setUsername(user.username().value());
        entity.setPasswordHash(user.password().getHashedValue());
        entity.setRoles(user.roles().stream()
                .map(Role::name)
                .collect(Collectors.toSet()));
        return entity;
    }
}
