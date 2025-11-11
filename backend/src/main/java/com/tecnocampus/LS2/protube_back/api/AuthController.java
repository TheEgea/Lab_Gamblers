package com.tecnocampus.LS2.protube_back.api;

import com.tecnocampus.LS2.protube_back.application.auth.AuthenticationService;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.application.dto.request.*;
import com.tecnocampus.LS2.protube_back.application.dto.response.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Value("${application.security.jwt.token-prefix:Bearer}")
    private String token;

    public AuthController(AuthenticationService AuthenticationService) {
        this.authenticationService = AuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {

            String token = authenticationService.login(
                    new Username(request.username()),
                    request.password()
            );
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, this.token + " " + token)
                    .body(new AuthResponse(token));
        } catch (AuthenticationService.InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            String token = authenticationService.register(
                    new Username(request.username()),
                    request.password(),
                    request.email()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.AUTHORIZATION, this.token + " " + token)
                    .body(new AuthResponse(token));
        } catch (AuthenticationService.UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/clean")
    public ResponseEntity<Void> cleanUsers() {
        authenticationService.cleanUsers();
        return ResponseEntity.noContent().build();
    }

}
