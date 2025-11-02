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
/*
    public Password {
        Objects.requireNonNull(value, "password cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("password cannot be blank");
        }

        // Si no está hasheada, la hasheamos automáticamente
        if (!isHashed(value)) {
            try {
                value = hashPassword(value);
            } catch (Exception e) {
                throw new RuntimeException("Error hashing password: " + e.getMessage(), e);
            }
        }
    }

 */

    /**
     * Detecta si la contraseña ya está hasheada
     * Un hash SHA-256 en Base64 tiene típicamente 44 caracteres
     */
    private boolean isHashed(String password) {
        if (password.length() != 44) {
            return false;
        }

        // Verifica si coincide con el patrón de Base64
        return BASE64_SHA256_PATTERN.matcher(password).matches();
    }

    /**
     * Hashea una contraseña usando SHA-256 y la codifica en Base64
     */
    private String hashPassword(String rawPassword) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(rawPassword.getBytes());
        return Base64.getEncoder().encodeToString(md.digest());
    }

    /**
     * Verifica si una contraseña raw coincide con el hash almacenado
     */
    public boolean matches(String rawPassword) {
        try {
            String hashedInput = hashPassword(rawPassword);
            return this.value.equals(hashedInput);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error verifying password: " + e.getMessage(), e);
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
        return "Password{****}" + "Real value for debugging: " + value;
    }
}
