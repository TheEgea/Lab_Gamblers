package com.tecnocampus.LS2.protube_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.api.GlobalExceptionHandler;
import com.tecnocampus.LS2.protube_back.application.dto.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@Import(TestSecurityConfig.class) // Import a test security configuration
class GlobalExceptionHandlerTests {
/*
    @Autowired
    private MockMvc mockMvc;

    private String token;

    @Autowired
    private ObjectMapper objectMapper;



    @BeforeEach
    @WithMockUser // Mock a user for authentication
    void setup() throws Exception {
        // Register a user and obtain a token
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andDo(result -> token = result.getResponse().getHeader("Authorization"));
    }

    @Test
    void testHandleUserAlreadyExistsException() throws Exception {
        // Attempt to register the same user to trigger UserAlreadyExistsException
        RegisterRequest duplicateRequest = new RegisterRequest("testUser", "password123");
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("USER_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("Username already exists"));
    }

 */
}