package com.tecnocampus.LS2.protube_back.domain.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

public record Password(String value) {
    // Patrón para detectar un hash SHA-256 en Base64 (44 caracteres con posible padding)
    private static final Pattern BASE64_SHA256_PATTERN =
            Pattern.compile("^[A-Za-z0-9+/]{42,44}={0,2}$");

    public Password {
        Objects.requireNonNull(value, "password cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("password cannot be blank");
        }

    }
    /**
     * Retorna el hash de la contraseña
     */
    public String getHashedValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Password{****}";
    }
}
