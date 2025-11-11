package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.application.auth.AuthenticationService;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.application.user.UserService;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    void testLoadByUsername_UserExists() {
        // Arrange
        String username = "testUser";
        User user = new User(new UserId(UUID.randomUUID()),new Username(username), new Password("password"),Role.USER ,"test@gmail.com");
        UserEntity userEntity = UserMapper.toEntity(user);
        userEntity.setUsername(username);
        when(userJpaRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        // Act
        Optional<User> result = userService.loadByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(username, result.get().username().value());
    }

    @Test
    void testLoadByUsername_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userJpaRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.loadByUsername(username);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testChangeRole_Success() {
        // Arrange
        String username = "testUser";
        String newRole = "ADMIN";
        User user = new User(new UserId(UUID.randomUUID()),new Username(username), new Password("password"),Role.USER ,"test@gmail.com");
        when(userJpaRepository.findByUsername(username)).thenReturn(Optional.of(UserMapper.toEntity(user)));
        when(authenticationService.generateToken(any())).thenReturn("newToken");

        // Act
        String token = userService.changeRole(username, newRole);

        // Assert
        assertEquals("newToken", token);
        verify(userJpaRepository).save(any());
    }

    @Test
    void testChangeRole_UserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userJpaRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.changeRole(username, "ADMIN"));
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        String username = "testUser";
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        User user = new User(new UserId(UUID.randomUUID()),new Username(username), new Password("password"),Role.USER ,"test@gmail.com");
        when(userJpaRepository.findByUsername(username)).thenReturn(Optional.of(UserMapper.toEntity(user)));
        when(authenticationService.samePassword(currentPassword, "password")).thenReturn(true);
        when(authenticationService.samePassword(newPassword, "password")).thenReturn(false);
        when(authenticationService.hashPassword(newPassword)).thenReturn("newHashedPassword");

        // Act
        userService.changePassword(username, currentPassword, newPassword);

        // Assert
        verify(userJpaRepository).save(any());
    }

    @Test
    void testChangePassword_CurrentPasswordIncorrect() {
        // Arrange
        String username = "testUser";
        String currentPassword = "wrongPassword";
        String newPassword = "newPassword";
        User user = new User(new UserId(UUID.randomUUID()),new Username(username), new Password("password"),Role.USER ,"test@gmail.com");
        when(userJpaRepository.findByUsername(username)).thenReturn(Optional.of(UserMapper.toEntity(user)));
        when(authenticationService.samePassword(currentPassword, "password")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.changePassword(username, currentPassword, newPassword));
    }

    @Test
    void testChangePassword_NewPasswordSameAsOld() {
        // Arrange
        String username = "testUser";
        String currentPassword = "currentPassword";
        String newPassword = "currentPassword";
        User user = new User(new UserId(UUID.randomUUID()),new Username(username), new Password("password"),Role.USER ,"test@gmail.com");
        when(userJpaRepository.findByUsername(username)).thenReturn(Optional.of(UserMapper.toEntity(user)));
        when(authenticationService.samePassword(currentPassword, "password")).thenReturn(true);
        when(authenticationService.samePassword(newPassword, "password")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.changePassword(username, currentPassword, newPassword));
    }
}