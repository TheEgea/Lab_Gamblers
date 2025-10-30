package com.tecnocampus.LS2.protube_back.persistence.jpa.user;

import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import com.tecnocampus.LS2.protube_back.web.dto.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class JpaUserAuthAdapter implements UserAuthPort {

    private final UserJpaRepository repo;
    private final PasswordEncoder encoder;

    public JpaUserAuthAdapter(UserJpaRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public Optional<User> loadByUsername(Username username) {
        return repo.findByUsername(username.value()).map(UserMapper::toUser);
    }
    @Override
    public boolean login(Username username, Password password) {
        return repo.login(username,password);
    }





}