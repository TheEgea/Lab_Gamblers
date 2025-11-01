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


    public String login(Username username, Password password) {
        User user = userAuthPort.loadByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        // ✅ CAMBIAR ESTO: usar la contraseña raw (sin la lógica de auto-hash de Password)
        // Crear un Password simple para la verificación
        String rawPassword = password.value(); // Obtener el valor original

        // Verificar usando el adapter que usa BCrypt
        if (!encoder.matches(rawPassword, user.password().value())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        Instant now = Instant.now();
        TokenClaims claims = new TokenClaims(
                username.value(),
                now,
                now.plus(10, ChronoUnit.HOURS),
                user.roles()
        );

        return tokenService.issue(claims);
    }


    public String register(Username username, Password password) {
        // Verificar si el usuario ya existe
        System.out.println("Checking if user exists: " + username.value());
        if (userAuthPort.loadByUsername(username).isPresent()) {
            System.out.println("User already exists: " + username.value());
            throw new UserAlreadyExistsException("Username already exists");
        }
        System.out.println("Registering user: " + username.value());

        // Crear nuevo usuario con rol USER por defecto
        User newUser = new User(
                new UserId(UUID.randomUUID()),
                username,
                password, // Ya se hashea automáticamente en el constructor
                Role.USER
        );

        // Guardar usuario (necesitamos añadir este méto_do al puerto)
        userAuthPort.save(newUser);

        return generateToken(newUser);
    }

    private String generateToken(User user) {
        Instant now = Instant.now();
        TokenClaims claims = new TokenClaims(
                user.username().value(),
                now,
                now.plus(10, ChronoUnit.HOURS),
                Collections.singleton(user.roles())
        );
        return tokenService.issue(claims);
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
