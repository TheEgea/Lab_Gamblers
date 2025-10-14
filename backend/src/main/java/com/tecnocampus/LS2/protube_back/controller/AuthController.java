package com.tecnocampus.LS2.protube_back.web.auth;

import com.tecnocampus.LS2.protube_back.application.auth.AuthService;
import com.tecnocampus.LS2.protube_back.web.auth.dto.AuthenticationRequest;
import com.tecnocampus.LS2.protube_back.web.auth.dto.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${application.security.jwt.token-prefix:Bearer}")
    private String tokenPrefix;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest req) {
        try {
            String token = authService.login(req.username(), req.password());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, tokenPrefix + " " + token)
                    .body(new AuthenticationResponse(token));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).build();
        }
    }
}