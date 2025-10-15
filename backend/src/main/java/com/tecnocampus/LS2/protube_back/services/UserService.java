package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.HashedPassword;
import com.tecnocampus.LS2.protube_back.domain.user.RawPassword;
import com.tecnocampus.LS2.protube_back.domain.user.User;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserAuthPort {

    private final UserJpaRepository userJpaRepository;

    public UserService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> loadByUsername(Username username) {
        return userJpaRepository.loadByUsername(username.toString());
    }

    @Override
    public boolean verifyPassword(RawPassword raw, HashedPassword hashed) {
        return userJpaRepository.verifyPassword(raw, hashed);
    }
}
