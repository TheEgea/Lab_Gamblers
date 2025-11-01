package com.tecnocampus.LS2.protube_back.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String credentials) {
        // Implementación temporal
        return ResponseEntity.ok("Login endpoint");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody String user) {
        // Implementación temporal
        return ResponseEntity.ok("Register endpoint");
    }
}
