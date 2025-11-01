package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.api.IndexController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndexControllerTest {
    IndexController indexController = new IndexController();

    @Test
    void home() {

        assertEquals("index", indexController.home().getViewName());
    }

    @Test
    void logout() {

        assertEquals("logout", indexController.logout().getViewName());
    }

    @Test
    void testing() {

        Assertions.assertTrue(true);
    }
}

