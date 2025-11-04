package com.tecnocampus.LS2.protube_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.exception.*;
import com.tecnocampus.LS2.protube_back.exception.user.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTests {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void testNotFoundException() {
        String url = "http://localhost:8080" + "/nonexistent/resource";

        try {
            restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(ex.getResponseBodyAsString()).contains("NOT_FOUND", "Resource not found");
        }
    }

    @Test
    void testUserAlreadyExistsException() {
        String url = "http://localhost:8080" + "/auth/register";
        String requestBody = "{\"username\": \"testUser\", \"password\": \"password123\"}";

        try {
            restTemplate.postForEntity(url, requestBody, String.class);
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(ex.getResponseBodyAsString()).contains("USER_ALREADY_EXISTS", "User already exists");
        }
    }

    @Test
    void testValidationException() {
        String url = "http://localhost:8080" + "/auth/register";
        String invalidRequestBody = "{\"username\": \"\", \"password\": \"short\"}";

        try {
            restTemplate.postForEntity(url, invalidRequestBody, String.class);
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(ex.getResponseBodyAsString()).contains("VALIDATION_ERROR", "Validation error");
        }
    }

    @Test
    void testAccessDeniedException() {
        String url = "http://localhost:8080" + "/protected/resource";

        try {
            restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(ex.getResponseBodyAsString()).contains("ACCESS_DENIED", "You don't have permission to access this resource");
        }
    }

    @Test
    void testIllegalArgumentException() {
        String url = "http://localhost:8080" + "/api/resource?invalidParam=abc";

        try {
            restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(ex.getResponseBodyAsString()).contains("INVALID_ARGUMENT", "Illegal argument");
        }
    }

    @Test
    void testGenericException() {
        String url = "http://localhost:8080" + "/api/resource/triggerError";

        try {
            restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException ex) {
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(ex.getResponseBodyAsString()).contains("INTERNAL_SERVER_ERROR", "An unexpected error occurred");
        }
    }
}