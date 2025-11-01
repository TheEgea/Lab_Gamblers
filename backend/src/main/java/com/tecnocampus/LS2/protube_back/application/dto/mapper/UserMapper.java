package com.tecnocampus.LS2.protube_back.application.dto.mapper;

import com.tecnocampus.LS2.protube_back.application.dto.request.AuthRequest;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserEntity;

import org.springframework.stereotype.Component;
import com.tecnocampus.LS2.protube_back.domain.user.*;

import java.util.Set;
import java.util.UUID;

@Component
public class UserMapper {
    public static User toDomain(AuthRequest loginRequest) {
        return new User(
                new UserId(UUID.randomUUID()),
                new Username(loginRequest.username()),
                new Password(loginRequest.password()),
                Role.USER
                );
    }

    public static AuthRequest toLoginRequest(User user) {
        return new AuthRequest(user.username().toString(),user.password().toString());
    }

    public static UserEntity toEntity(User domain) {
        return new UserEntity(
                domain.id().value(),
                domain.username().toString(),
                domain.password().toString(),
                domain.roles().toString()
        );
    }

    public static User toUser(UserEntity domain) {
        return new User(
             new UserId(domain.getId()),
             new Username(domain.getUsername()),
             new Password(domain.getPasswordHash()),
                domain.getRole()
        );
    }
}
