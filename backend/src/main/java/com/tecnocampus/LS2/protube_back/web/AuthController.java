package com.tecnocampus.LS2.protube_back.web;

import com.tecnocampus.LS2.protube_back.web.dto.request.LoginRequest;
import com.tecnocampus.LS2.protube_back.web.dto.response.LoginResponse;
import com.tecnocampus.LS2.protube_back.application.auth.LoginService;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.domain.user.RawPassword;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        Username username = new Username(request.username());
        RawPassword password = new RawPassword(request.password());
        String token = loginService.login(username, password);
        return new LoginResponse(token);
    }
}
