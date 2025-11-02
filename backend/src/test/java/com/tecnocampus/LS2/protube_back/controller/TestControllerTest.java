
package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.api.TestController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TestControllerTest {

    @InjectMocks
    TestController testController;

    @Test
    void sayHello() {

        String s = testController.sayHello();
        assertEquals("Hello, World!", s);
    }
}
