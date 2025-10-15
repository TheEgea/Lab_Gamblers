package com.tecnocampus.LS2.protube_back.domain.user;

import java.util.Objects;

public record HashedPassword(String value) {
    public HashedPassword {
        Objects.requireNonNull(value, "hashed password cannot be null");
        if (value.isBlank()) throw new IllegalArgumentException("hashed password cannot be blank");
    }
    @Override public String toString() { return "********"; }
}