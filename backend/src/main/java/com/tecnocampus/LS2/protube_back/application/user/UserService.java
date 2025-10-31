package com.tecnocampus.LS2.protube_back.application.user;

import com.tecnocampus.LS2.protube_back.application.auth.AuthenticationService;
import com.tecnocampus.LS2.protube_back.application.dto.request.AuthRequest;
import com.tecnocampus.LS2.protube_back.application.dto.response.AuthResponse;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.JpaUserAuthAdapter;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserJpaRepository;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationService loginService;
    private final UserEntity userEntity;
    private final JpaUserAuthAdapter jpaUserAuthAdapter;
    private final UserJpaRepository userRepository;

    public void save(AuthRequest loginRequest) {
        userRepository.save(UserMapper
                .toEntity(UserMapper
                                .toDomain(loginRequest)));

        //User u = UserMapper.toDomain(loginRequest);
    }

    public AuthResponse login(AuthRequest loginRequest) {
        Username username = new Username(loginRequest.username());



    }


}
