package com.tecnocampus.LS2.protube_back.application.dto.mapper;

import com.tecnocampus.LS2.protube_back.application.dto.request.AuthRequest;
import com.tecnocampus.LS2.protube_back.application.dto.response.UserResponse;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserEntity;

import org.springframework.stereotype.Component;
import com.tecnocampus.LS2.protube_back.domain.user.*;


import java.util.UUID;

@Component
public class UserMapper {
    public static User toDomain(UserEntity entity) {
        return new User(
                new UserId(entity.getId()),
                new Username(entity.getUsername()),
                new Password(entity.getPasswordHash()),
                entity.getRole(),
                entity.getEmail()
        );
    }

    public static UserEntity toEntity(User domain) {
        return new UserEntity(
                domain.id().value(),
                domain.username().toString(),
                domain.password().getHashedValue(),
                domain.roles().toString(),
                domain.email()
        );
    }

    public static UserResponse toUserResponse(User domain) {
        return new UserResponse(
                domain.id().value().toString(),
                domain.username().toString(),
                domain.email(),
                domain.roles().toString()
        );
    }
}
