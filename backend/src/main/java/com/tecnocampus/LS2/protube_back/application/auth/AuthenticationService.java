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
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Username: " + username.value());
        System.out.println("Raw password: '" + rawPassword + "'");

        User user = userAuthPort.loadByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        System.out.println("Usuario encontrado. Hash en DB: " + user.password().value());

        // ✅ Verificar usando BCrypt: raw vs hash guardado
        boolean matches = encoder.matches(rawPassword, user.password().value());
        System.out.println("¿Password coincide? " + matches);
        System.out.println("Encoder usado: " + encoder.getClass().getSimpleName());

        if (!matches) {
            System.out.println("❌ LOGIN FAILED: Password no coincide");
            throw new InvalidCredentialsException("Invalid username or password");
        }

        System.out.println("✅ LOGIN SUCCESS");
        return generateToken(user);
    }

    public String register(Username username, String rawPassword) { // ✅ Corregir parámetros
        System.out.println("=== REGISTRO USUARIO ===");
        System.out.println("Username: " + username.value());
        System.out.println("Raw password: '" + rawPassword + "'");

        // Verificar si el usuario ya existe
        if (userAuthPort.loadByUsername(username).isPresent()) {
            System.out.println("❌ Usuario ya existe: " + username.value());
            throw new UserAlreadyExistsException("Username already exists");
        }

        // ✅ Hashear con BCrypt
        String hashedPassword = encoder.encode(rawPassword);
        System.out.println("Password hasheada con BCrypt: " + hashedPassword);

        // ✅ Crear usuario con hash BCrypt
        User newUser = new User(
                new UserId(UUID.randomUUID()),
                username,
                new Password(hashedPassword), // Hash ya hecho con BCrypt
                Role.USER
        );

        System.out.println("Guardando usuario: " + newUser.username().value());
        userAuthPort.save(newUser);

        System.out.println("✅ Usuario registrado correctamente");
        return generateToken(newUser);
    }

    private String generateToken(User user) {
        System.out.println("=== GENERANDO TOKEN ===");
        Instant now = Instant.now();
        TokenClaims claims = new TokenClaims(
                user.username().value(),
                now,
                now.plus(10, ChronoUnit.HOURS),
                Collections.singleton(user.roles()) // ✅ Arreglar si roles es Set
        );

        String token = tokenService.issue(claims);
        System.out.println("Token generado: " + token.substring(0, 20) + "...");
        return token;
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
