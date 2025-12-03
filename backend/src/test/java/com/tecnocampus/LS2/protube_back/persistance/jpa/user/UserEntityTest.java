package com.tecnocampus.LS2.protube_back.persistance.jpa.user;

import com.tecnocampus.LS2.protube_back.domain.user.Role;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void testUserEntityConstructorValues() {
        // Arrange
        UUID id = UUID.randomUUID();
        String username = "testUser";
        String passwordHash = "hashedPassword";
        String role = "ADMIN";
        String email = "test@example.com";

        // Act
        UserEntity userEntity = new UserEntity(id, username, passwordHash, role, email);

        // Assert
        assertEquals(id, userEntity.getId());
        assertEquals(username, userEntity.getUsername());
        assertEquals(passwordHash, userEntity.getPasswordHash());
        assertEquals(Role.ADMIN, userEntity.getRole());
        assertEquals(email, userEntity.getEmail());
    }

    @Test
    void testUserEntityModifiedValues() {
        // Arrange
        UserEntity userEntity = new UserEntity(
                UUID.randomUUID(), "testUser", "hashedPassword", "ADMIN", "test@example.com"
        );

        UUID newId = UUID.randomUUID();
        String newUsername = "newUser";
        String newPasswordHash = "newHashedPassword";
        String newRole = "USER";
        String newEmail = "new@example.com";

        // Act
        userEntity.setId(newId);
        userEntity.setUsername(newUsername);
        userEntity.setPasswordHash(newPasswordHash);
        userEntity.setRole(newRole);
        userEntity.setEmail(newEmail);

        // Assert
        assertEquals(newId, userEntity.getId());
        assertEquals(newUsername, userEntity.getUsername());
        assertEquals(newPasswordHash, userEntity.getPasswordHash());
        assertEquals(Role.USER, userEntity.getRole());
        assertEquals(newEmail, userEntity.getEmail());
    }

    @Test
    void testConstructorWithoutArguments() {
        // Act
        UserEntity userEntity = new UserEntity();

        // Assert
        assertNull(userEntity.getId());
        assertNull(userEntity.getUsername());
        assertNull(userEntity.getPasswordHash());
        assertNull(userEntity.getEmail());
        
    }
}