package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        Optional<UserEntity> userEntity = repo.findByUsername(username.value());
        return userEntity.map(UserMapper::toDomain);
    }


    @Override
    public void save(User user) {
        repo.save(UserMapper.toEntity(user));
    }

    @Override
    public void deleteAllUsers() {
        repo.deleteAll();
    }
}
