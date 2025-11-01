package com.tecnocampus.LS2.protube_back.domain.user;

import java.util.Objects;

public record Password(String value) {
    public Password {
        Objects.requireNonNull(value, "password cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("password cannot be blank");
        }
    }

    @Override
    public String toString() {
        return "********";
    }
}
