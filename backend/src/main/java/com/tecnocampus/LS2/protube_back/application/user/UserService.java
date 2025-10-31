package com.tecnocampus.LS2.protube_back.application.user;

import com.tecnocampus.LS2.protube_back.application.auth.LoginService;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.JpaUserAuthAdapter;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.user.UserJpaRepository;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.request.LoginRequest;
import com.tecnocampus.LS2.protube_back.web.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final LoginService loginService;
    private final UserEntity userEntity;
    private final JpaUserAuthAdapter jpaUserAuthAdapter;
    private final UserJpaRepository userRepository;

    public void save(LoginRequest loginRequest) {
        userRepository.save(UserMapper
                .toEntity(UserMapper
                                .toDomain(loginRequest)));

        //User u = UserMapper.toDomain(loginRequest);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Username username = new Username(loginRequest.username());



    }


}
