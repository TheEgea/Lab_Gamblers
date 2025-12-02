package com.tecnocampus.LS2.protube_back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.api.AuthController;
import com.tecnocampus.LS2.protube_back.application.auth.AuthenticationService;
import com.tecnocampus.LS2.protube_back.application.dto.request.AuthRequest;
import com.tecnocampus.LS2.protube_back.application.dto.request.RegisterRequest;
import com.tecnocampus.LS2.protube_back.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(JwtTokenService.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
public class AuthControllerTest {

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin() throws Exception {
        String token = "mocked-jwt-token";
        Mockito.when(authenticationService.login(any(), eq("password123"))).thenReturn(token);

        AuthRequest authRequest = new AuthRequest("testUser", "password123","test@gmail.com");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.token").value(token));
    }


    @Test
    void testRegister() throws Exception {
        String token = "mocked-jwt-token";
        Mockito.when(authenticationService.register(any(), eq("password123"),any())).thenReturn(token);

        RegisterRequest registerRequest = new RegisterRequest("testUser", "password123","test@gmail.com");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        Mockito.when(authenticationService.login(any(), eq("wrongPassword")))
                .thenThrow(new AuthenticationService.InvalidCredentialsException("Invalid username or password"));

        AuthRequest authRequest = new AuthRequest("testUser", "wrongPassword","test@gmail.com");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegisterUserAlreadyExists() throws Exception {
        Mockito.when(authenticationService.register(any(), eq("password123"),any()))
                .thenThrow(new AuthenticationService.UserAlreadyExistsException("Username already exists"));

        RegisterRequest registerRequest = new RegisterRequest("existingUser", "password123","test@gmail.com");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void testClean() throws Exception {
        doNothing().when(authenticationService).cleanUsers();

        mockMvc.perform(post("/auth/clean")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}