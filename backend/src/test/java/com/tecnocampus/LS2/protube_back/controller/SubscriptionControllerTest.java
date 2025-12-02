package com.tecnocampus.LS2.protube_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.api.SubscriptionController;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionRequest;
import com.tecnocampus.LS2.protube_back.application.dto.subscription.SubscriptionResponse;
import com.tecnocampus.LS2.protube_back.application.subscription.SubscriptionService;
import com.tecnocampus.LS2.protube_back.application.user.UserService;
import com.tecnocampus.LS2.protube_back.domain.user.*;
import com.tecnocampus.LS2.protube_back.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
public class SubscriptionControllerTest {
/*
    @MockBean
    private SubscriptionService subscriptionService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testUser")
    void testSubscribeWithAuthenticationAndRequestBody() throws Exception {
        // Crear el cuerpo de la solicitud
        SubscriptionRequest request = new SubscriptionRequest("channelName");
        SubscriptionResponse response = new SubscriptionResponse(
                1L,
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "channelName",
                "2023-01-01T12:00:00"
        );

        // Simular el comportamiento del servicio
        when(userService.loadByUsername("testUser"))
                .thenReturn(Optional.of(new User(
                        new UserId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                        new Username("testUser"),
                        new Password("ValidPassword1!"),
                        Role.USER,
                        "test@gmail.com"
                )));
        when(subscriptionService.subscribe(any(UUID.class), eq(request))).thenReturn(response);

        // Realizar la solicitud y verificar el resultado
        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.channelName").value("channelName"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.subscribedAt").value("2023-01-01T12:00:00"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testUnsubscribe() throws Exception {
        mockMvc.perform(delete("/api/subscriptions/channelName"))
                .andExpect(status().isNoContent());

        Mockito.verify(subscriptionService).unsubscribe(any(UUID.class), eq("channelName"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testGetUserSubscriptions() throws Exception {
        SubscriptionResponse response = createSubscriptionResponse();

        when(subscriptionService.getUserSubscriptions(any(UUID.class)))
                .thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].channelName").value("channelName"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testIsSubscribed() throws Exception {
        when(subscriptionService.isSubscribed(any(UUID.class), eq("channelName"))).thenReturn(true);

        mockMvc.perform(get("/api/subscriptions/check/channelName"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    private SubscriptionResponse createSubscriptionResponse() {
        return new SubscriptionResponse(
                1L, // id
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), // userId
                "exampleChannel", // channelName
                "2023-01-01T12:00:00" // subscribedAt
        );
    }


 */
}