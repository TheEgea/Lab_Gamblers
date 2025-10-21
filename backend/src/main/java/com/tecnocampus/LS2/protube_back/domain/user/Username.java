package com.tecnocampus.LS2.protube_back.domain.user;

import java.util.Objects;

public record Username(String value) {
    public Username {
        Objects.requireNonNull(value, "username cannot be null");
        if (value.isBlank()) throw new IllegalArgumentException("username cannot be blank");
    }
    @Override public String toString() { return value; }
}