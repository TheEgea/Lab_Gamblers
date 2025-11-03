package com.tecnocampus.LS2.protube_back.domain;

import com.tecnocampus.LS2.protube_back.domain.user.Password;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void validPassword() {
        Password password = new Password("ValidPassword123");
        assertNotNull(password);
        assertEquals("ValidPassword123", password.getHashedValue());
    }

    @Test
    void nullPasswordThrowsException() {
        assertThrows(NullPointerException.class, () -> new Password(null));
    }

    @Test
    void blankPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Password("   "));
    }

    @Test
    void toStringHidesPasswordValue() {
        Password password = new Password("ValidPassword123");
        assertEquals("Password{****}", password.toString());
    }
}