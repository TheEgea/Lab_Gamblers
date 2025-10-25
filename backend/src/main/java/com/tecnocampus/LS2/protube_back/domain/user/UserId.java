package com.tecnocampus.LS2.protube_back.domain.user;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {
    public UserId { Objects.requireNonNull(value, "UserId cannot be null"); }
    public static UserId random() { return new UserId(UUID.randomUUID()); }
    @Override public String toString() { return value.toString(); }
}