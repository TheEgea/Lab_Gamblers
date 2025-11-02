package com.tecnocampus.LS2.protube_back.application.user;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.User;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    //TODO: Comprobar si es necesaria esta clase? solo se utiliza en tests .-.

    private final UserJpaRepository userJpaRepository;

    public UserService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public Optional<User> loadByUsername(String username) {

        //TODO:Checkear
        Optional<UserEntity> userEntityOptional = userJpaRepository.findByUsername(username);

        return userEntityOptional.map(UserMapper::toDomain);


    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        // TODO: Implementar verificación de password
        return rawPassword.equals(hashedPassword);
    }
}
