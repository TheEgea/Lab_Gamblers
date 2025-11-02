package com.tecnocampus.LS2.protube_back.application.auth;

import com.tecnocampus.LS2.protube_back.domain.auth.TokenClaims;
import com.tecnocampus.LS2.protube_back.domain.auth.TokenService;
import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserAuthPort userAuthPort;
    private final TokenService tokenService;
    private final PasswordEncoder encoder; // ✅ Añadir esto

    public AuthenticationService(UserAuthPort userAuthPort, TokenService tokenService, PasswordEncoder encoder) {
        this.userAuthPort = userAuthPort;
        this.tokenService = tokenService;
        this.encoder = encoder; // ✅ Añadir al constructor
    }

    public String login(Username username, String rawPassword) { // ✅ Cambiar a String rawPassword


        User user = userAuthPort.loadByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));


        if (samePassword(rawPassword, user.password().value())) {
            return generateToken(user);
        }

        throw new InvalidCredentialsException("Invalid username or password");


    }

    public String register(Username username, String rawPassword) { // ✅ Corregir parámetros


        // Verificar si el usuario ya existe
        if (userAuthPort.loadByUsername(username).isPresent()) {

            throw new UserAlreadyExistsException("Username already exists");
        }

        // ✅ Hashear con BCrypt
        String hashedPassword = hashPassword(rawPassword);


        // ✅ Crear usuario con hash BCrypt
        User newUser = new User(
                new UserId(UUID.randomUUID()),
                username,
                new Password(hashedPassword), // Hash ya hecho con BCrypt
                Role.USER
        );


        userAuthPort.save(newUser);


        return generateToken(newUser);
    }

    public String generateToken(User user) {

        Instant now = Instant.now();
        TokenClaims claims = new TokenClaims(
                user.username().value(),
                now,
                now.plus(10, ChronoUnit.HOURS),
                Collections.singleton(user.roles()) // ✅ Arreglar si roles es Set
        );

        String token = tokenService.issue(claims);

        return token;
    }

    public boolean samePassword(String rawPassword, String hashedPassword) {
        return encoder.matches(hashPassword(rawPassword), hashedPassword);
    }

    public String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
