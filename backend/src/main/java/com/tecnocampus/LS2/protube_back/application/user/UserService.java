package com.tecnocampus.LS2.protube_back.application.video;

import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.User;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserJpaRepository userJpaRepository;

    public UserService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public Optional<User> loadByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(entity -> {
                    // TODO: Convertir UserEntity a User domain
                    return null;
                });
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        // TODO: Implementar verificación de password
        return rawPassword.equals(hashedPassword);
    }
}
