package com.tecnocampus.LS2.protube_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.api.UserController;
import com.tecnocampus.LS2.protube_back.application.dto.request.AuthRequest;
import com.tecnocampus.LS2.protube_back.application.dto.response.AuthResponse;
import com.tecnocampus.LS2.protube_back.application.dto.response.UserResponse;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionResponse;
import com.tecnocampus.LS2.protube_back.application.subscription.SubscriptionService;
import com.tecnocampus.LS2.protube_back.application.user.UserService;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.UserMapper;
import com.tecnocampus.LS2.protube_back.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private SubscriptionService subscriptionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUpgradeToAdmin() throws Exception {
        String token = "mocked-jwt-token";
        when(userService.changeRole(eq("testUser"), eq("adminKey"))).thenReturn(token);

        mockMvc.perform(post("/users/changeRole")
                        .param("username", "testUser")
                        .param("RoleKey", "adminKey"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void testChangePassword() throws Exception {
        AuthRequest authRequest = new AuthRequest("testUser", "ValidOldPassword1@", "test@gmail.com");

        Mockito.doNothing().when(userService).changePassword(any(), any(), any());

        mockMvc.perform(post("/users/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest))
                        .param("newPassword", "ValidNewPassword1@"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));
    }

    @Test
    void testGetUsernameFromToken() throws Exception {
        String token = "mocked-jwt-token";
        String username = "testUser";

        User user = new User(new UserId(UUID.randomUUID()),new Username(username), new Password("12345678aA!"),Role.USER,"test@gmail.com");

        when(jwtTokenService.getUsernameFromToken(eq(token))).thenReturn(username);
        when(userService.loadByUsername(username)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/u")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
    @Test
    void testGetCurrentUserProfile() throws Exception {
        // Mock data
        String username = "testUser";
        UUID userId = UUID.randomUUID();
        User user = new User(new UserId(userId), new Username(username), new Password("12345678aA!"), Role.USER, "test@gmail.com");

        List<SubscriptionResponse> subscriptions = List.of(
                createSubscriptionResponse("Channel1", userId),
                createSubscriptionResponse("Channel2",userId)
        );

        // Mocking services
        when(userService.loadByUsername(any())).thenReturn(Optional.of(user));
        when(subscriptionService.getUserSubscriptions(userId)).thenReturn(subscriptions);

        // Perform request
        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.roles").value("USER"))
                .andExpect(jsonPath("$.subscriptionCount").value(2))
                .andExpect(jsonPath("$.subscribedChannels[0]").value("Channel1"));
    }
    private SubscriptionResponse createSubscriptionResponse(String channelName, UUID userId) {
        return new SubscriptionResponse(1L, userId, channelName,"2-12-2025");
    }
}