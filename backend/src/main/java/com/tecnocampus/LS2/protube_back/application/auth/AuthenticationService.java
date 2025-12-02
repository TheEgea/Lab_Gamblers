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
    private final PasswordEncoder encoder;

    public AuthenticationService(UserAuthPort userAuthPort, TokenService tokenService, PasswordEncoder encoder) {
        this.userAuthPort = userAuthPort;
        this.tokenService = tokenService;
        this.encoder = encoder;
    }

    public String login(Username username, String rawPassword) {
        User user = userAuthPort.loadByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (samePassword(rawPassword, user.password().value())) {
            return generateToken(user);
        }
        throw new InvalidCredentialsException("Invalid username or password");
    }

    public String register(Username username, String rawPassword, String email) {
        // Verificar si el usuario ya existe
        if (userAuthPort.loadByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        String hashedPassword = hashPassword(rawPassword);

        User newUser = new User(
                new UserId(UUID.randomUUID()),
                username,
                new Password(hashedPassword), // Hash ya hecho con BCrypt
                Role.USER,
                email

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
                Collections.singleton(user.roles())
        );

        return tokenService.issue(claims);
    }

    public boolean samePassword(String rawPassword, String hashedPassword) {
                return encoder.matches(rawPassword, hashedPassword);
    }

    public String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public void cleanUsers() {
        userAuthPort.deleteAllUsers();
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
