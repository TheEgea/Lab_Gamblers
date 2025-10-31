package com.tecnocampus.LS2.protube_back.application.auth;

import com.tecnocampus.LS2.protube_back.domain.auth.TokenClaims;
import com.tecnocampus.LS2.protube_back.domain.auth.TokenService;
import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.Password;
import com.tecnocampus.LS2.protube_back.domain.user.User;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.request.LoginRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class LoginService {

    private final UserAuthPort userAuthPort;
    private final TokenService tokenService;

    public LoginService(UserAuthPort userAuthPort, TokenService tokenService) {
        this.userAuthPort = userAuthPort;
        this.tokenService = tokenService;
    }

    public String login(Username username, Password password) {



        User user = userAuthPort.loadByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));



        if (!userAuthPort.login(username,password)) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        Instant now = Instant.now();
        TokenClaims claims = new TokenClaims(
                username.value(),
                now,
                now.plus(10, ChronoUnit.HOURS),
                user.roles()
        );

        return tokenService.issue(claims);
    }

    public String register(LoginRequest request) {
        return null;
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }
}
