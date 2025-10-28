package com.tecnocampus.LS2.protube_back.web;

<<<<<<< HEAD
import com.tecnocampus.LS2.protube_back.web.dto.request.AuthenticationRequest;
import com.tecnocampus.LS2.protube_back.web.dto.response.AuthenticationResponse;
import com.tecnocampus.LS2.protube_back.domain.auth.TokenService;
import com.tecnocampus.LS2.protube_back.domain.auth.TokenClaims;
import com.tecnocampus.LS2.protube_back.domain.user.Role;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Set;
=======
import com.tecnocampus.LS2.protube_back.application.auth.LoginService;
import com.tecnocampus.LS2.protube_back.domain.user.RawPassword;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.web.dto.request.LoginRequest;
import com.tecnocampus.LS2.protube_back.web.dto.response.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
>>>>>>> manual-auth-refactor-clean

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

<<<<<<< HEAD
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
=======
    @Value("${application.security.jwt.token-prefix:Bearer}")
    private String tokenPrefix;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
>>>>>>> manual-auth-refactor-clean
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        TokenClaims claims = new TokenClaims(
            authentication.getName(),
            Instant.now(),
            Instant.now().plusSeconds(3600),
            Set.of(Role.USER)
        );

        String token = tokenService.issue(claims);
        return new AuthenticationResponse(token);
    }
}
