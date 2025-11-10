package com.tecnocampus.LS2.protube_back.domain;

import com.tecnocampus.LS2.protube_back.domain.user.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UserTest {

    @Test
    void testGetters() {
        User user = createUser();
        assert user.username().value().equals("username");
        assert user.password().value().equals("password");
        assert user.roles() == Role.ADMIN;
        assert user.email().equals("test@gmail.com");
    }

    @Test
    void testToString(){
        User user = createUser();
        String userString = user.toString();
        assert userString.equals("User{" +
                "id=" + user.id().toString() +
                ", username=" + user.username() +
                ", password=" + user.password() +
                ", roles=" + user.roles() +
                '}');
    }

    @Test
    void testSetPassword(){
        User user = createUser();
        user.setPassword("newPassword");
        assert user.password().value().equals("newPassword");

    }

    private User createUser(){
        return new User(new UserId(UUID.randomUUID()),
                new Username("username"),
                new Password("password"),
                Role.ADMIN,"test@gmail.com");
    }
}
