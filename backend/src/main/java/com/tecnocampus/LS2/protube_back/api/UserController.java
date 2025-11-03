package com.tecnocampus.LS2.protube_back.api;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.application.dto.request.AuthRequest;
import com.tecnocampus.LS2.protube_back.application.dto.response.AuthResponse;
import com.tecnocampus.LS2.protube_back.application.dto.response.UserResponse;
import com.tecnocampus.LS2.protube_back.application.user.UserService;
import com.tecnocampus.LS2.protube_back.security.jwt.JwtTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/ChangeRole")
    public ResponseEntity<AuthResponse> upgradeToAdmin(@Valid String username, @Valid String RoleKey) {
        String token = userService.changeRole(username, RoleKey);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new AuthResponse(token));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody AuthRequest authRequest, @Valid String newPassword) {
        userService.changePassword(authRequest.username(), authRequest.password(), newPassword);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Password changed successfully");
    }

    @GetMapping("/u")
    public ResponseEntity<UserResponse> getUsernameFromToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String username = jwtTokenService.getUsernameFromToken(token);
        UserResponse user = UserMapper.toUserResponse(userService.loadByUsername(username).orElseThrow());
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

}
