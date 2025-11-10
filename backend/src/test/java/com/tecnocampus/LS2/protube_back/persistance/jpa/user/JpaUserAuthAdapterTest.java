package com.tecnocampus.LS2.protube_back.persistance.jpa.user;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.JpaUserAuthAdapter;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaUserAuthAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private JpaUserAuthAdapter jpaUserAuthAdapter;

    @Test
    void testLoadByUsername_UserExists() {

        String username = "testUser";

        when(userJpaRepository.findByUsername(any())).thenReturn(Optional.of(new UserEntity(
                UUID.randomUUID(),
                username,
                "hashedPassword",
                "USER",
                "test@gmail.com"
        )));


        Optional<User> result = jpaUserAuthAdapter.loadByUsername(new Username(username));

        assertTrue(result.isPresent());
        assertEquals(username, result.get().username().value());
    }

    @Test
    void testLoadByUsername_UserNotFound() {

        String username = "nonExistentUser";

        when(userJpaRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = jpaUserAuthAdapter.loadByUsername(new Username(username));

        assertFalse(result.isPresent());
    }

    @Test
    void testSave() {

        User user = new User(
                new UserId(UUID.randomUUID()),
                new Username("testUser"),
                new Password("hashedPassword"),
                Role.USER,
                "test@example.com"
        );

        UserEntity userEntity = UserMapper.toEntity(user);

        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        jpaUserAuthAdapter.save(user);


    }

    @Test
    void testDeleteAllUsers() {
        jpaUserAuthAdapter.deleteAllUsers();
        verify(userJpaRepository).deleteAll();

    }
}