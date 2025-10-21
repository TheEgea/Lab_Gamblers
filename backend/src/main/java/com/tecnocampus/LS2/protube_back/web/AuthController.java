package com.tecnocampus.LS2.protube_back.web;

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

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;

    @Value("${application.security.jwt.token-prefix:Bearer}")
    private String tokenPrefix;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            String token = loginService.login(
                    new Username(request.username()),
                    new RawPassword(request.password())
            );
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, tokenPrefix + " " + token)
                    .body(new LoginResponse(token));
        } catch (LoginService.InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
