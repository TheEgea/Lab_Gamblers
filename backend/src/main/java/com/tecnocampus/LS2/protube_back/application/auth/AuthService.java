package com.tecnocampus.LS2.protube_back.application.auth;

import com.tecnocampus.LS2.protube_back.domain.auth.TokenClaims;
import com.tecnocampus.LS2.protube_back.domain.auth.TokenService;
import com.tecnocampus.LS2.protube_back.domain.auth.UserAuthPort;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final UserAuthPort userAuth;
    private final TokenService tokenService;

    public AuthService(UserAuthPort userAuth, TokenService tokenService) {
        this.userAuth = userAuth;
        this.tokenService = tokenService;
    }

    public String login(String usernameRaw, String passwordRaw) {
        Username username = new Username(usernameRaw);
        RawPassword raw = new RawPassword(passwordRaw);

        var user = userAuth.loadByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("bad_credentials"));

        if (!userAuth.verifyPassword(raw, user.password())) {
            throw new IllegalArgumentException("bad_credentials");
        }

        Instant now = Instant.now();
        TokenClaims claims = new TokenClaims(
                user.username().value(),
                now,
                now.plus(10, ChronoUnit.HOURS),
                user.roles()
        );
        return tokenService.issue(claims);
    }
}