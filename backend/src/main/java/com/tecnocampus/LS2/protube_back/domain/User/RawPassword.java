package com.tecnocampus.LS2.protube_back.domain.user;

import java.util.Objects;

public record RawPassword(String value) {
    public RawPassword {
        Objects.requireNonNull(value, "password cannot be null");
        if (value.isBlank()) throw new IllegalArgumentException("password cannot be blank");
    }
}