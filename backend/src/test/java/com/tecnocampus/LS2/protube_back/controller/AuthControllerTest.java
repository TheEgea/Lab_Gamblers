package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.api.AuthController;
import com.tecnocampus.LS2.protube_back.application.auth.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    void testLogin() {



    }

}
