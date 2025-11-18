package com.tecnocampus.LS2.protube_back.application.user;

import com.tecnocampus.LS2.protube_back.application.auth.AuthenticationService;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.application.dto.response.UserResponse;
import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.Password;
import com.tecnocampus.LS2.protube_back.domain.user.Role;
import com.tecnocampus.LS2.protube_back.domain.user.User;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    //TODO: Comprobar si es necesaria esta clase? solo se utiliza en tests .-.

    private final UserJpaRepository userJpaRepository;
    private final AuthenticationService authenticationService;

    public UserService(UserJpaRepository userJpaRepository, AuthenticationService authenticationService) {
        this.userJpaRepository = userJpaRepository;
        this.authenticationService = authenticationService;
    }

    public Optional<User> loadByUsername(String username) {

        //TODO:Checkear
        Optional<UserEntity> userEntityOptional = userJpaRepository.findByUsername(username);

        return userEntityOptional.map(UserMapper::toDomain);

    }

    @Transactional//Modifica datos en la base de datos
    public String changeRole(String username, String newRole) {
        User user = loadByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = Role.valueOf(newRole);
        user.setRoles(role);
        userJpaRepository.save(UserMapper.toEntity(user));
        return authenticationService.generateToken(user);
    }
    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = loadByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        if (!authenticationService.samePassword(currentPassword, user.password().value())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }


        if (authenticationService.samePassword(newPassword, user.password().value())) {
            throw new IllegalArgumentException("New password must be different from the old one");
        }


        String hashedPassword = authenticationService.hashPassword(newPassword);

        // ✅ Crear usuario actualizado con nueva contraseña
        User updatedUser = new User(
                user.id(),
                user.username(),
                new Password(hashedPassword), // Nueva contraseña hasheada
                user.roles(),
                user.email()
        );

        // ✅ Guardar en base de datos
        userJpaRepository.save(UserMapper.toEntity(updatedUser));
    }

    public Optional<UserResponse> loadById(UUID id) {
        User user = userJpaRepository.findById(id)
                .map(UserMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserResponse userResponse = UserMapper.toUserResponse(user);
        return Optional.of(userResponse);
    }

}
