package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.application.auth.AuthenticationService;
import com.tecnocampus.LS2.protube_back.domain.auth.TokenClaims;
import com.tecnocampus.LS2.protube_back.domain.auth.TokenService;
import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserAuthPort userAuthPort;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void login_ValidCredentials_ReturnsToken() {
        Username username = new Username("testUser");
        String rawPassword = "password";
        String hashedPassword = "hashedPassword";
        User user = new User(new UserId(UUID.randomUUID()), username, new Password(hashedPassword), Role.USER);

        when(userAuthPort.loadByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
        when(tokenService.issue(any(TokenClaims.class))).thenReturn("mockToken");

        String token = authenticationService.login(username, rawPassword);

        assertNotNull(token);
        assertEquals("mockToken", token);
        verify(userAuthPort).loadByUsername(username);
        verify(passwordEncoder).matches(rawPassword, hashedPassword);
        verify(tokenService).issue(any(TokenClaims.class));
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        Username username = new Username("testUser");
        String rawPassword = "password";

        when(userAuthPort.loadByUsername(username)).thenReturn(Optional.empty());

        assertThrows(AuthenticationService.InvalidCredentialsException.class,
                () -> authenticationService.login(username, rawPassword));

        verify(userAuthPort).loadByUsername(username);
        verifyNoInteractions(passwordEncoder, tokenService);
    }

    @Test
    void register_NewUser_ReturnsToken() {
        Username username = new Username("newUser");
        String rawPassword = "password";
        String hashedPassword = "hashedPassword";

        when(userAuthPort.loadByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);
        when(tokenService.issue(any(TokenClaims.class))).thenReturn("mockToken");

        String token = authenticationService.register(username, rawPassword);

        assertNotNull(token);
        assertEquals("mockToken", token);
        verify(userAuthPort).loadByUsername(username);
        verify(passwordEncoder).encode(rawPassword);
        verify(userAuthPort).save(any(User.class));
        verify(tokenService).issue(any(TokenClaims.class));
    }

    @Test
    void register_ExistingUser_ThrowsException() {
        Username username = new Username("existingUser");
        String rawPassword = "password";
        User existingUser = new User(new UserId(UUID.randomUUID()), username, new Password("hashedPassword"), Role.USER);

        when(userAuthPort.loadByUsername(username)).thenReturn(Optional.of(existingUser));

        assertThrows(AuthenticationService.UserAlreadyExistsException.class,
                () -> authenticationService.register(username, rawPassword));

        verify(userAuthPort).loadByUsername(username);
        verifyNoInteractions(passwordEncoder, tokenService);
    }
}