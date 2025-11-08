// backend/src/main/java/com/tecnocampus/LS2/protube_back/domain/video/VideoId.java
package com.tecnocampus.LS2.protube_back.domain.video;

import com.fasterxml.jackson.annotation.JsonCreator;  // ← AÑADIR
import com.fasterxml.jackson.annotation.JsonValue;    // ← AÑADIR
import java.util.Objects;
import java.util.UUID;

public final class VideoId {

    private final UUID value;

    public VideoId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    @JsonCreator  // ← AÑADIR
    public VideoId(String value) {
        this(UUID.fromString(Objects.requireNonNull(value)));
    }

    public static VideoId fromString(String value) {
        return new VideoId(value);
    }

    public static VideoId generate() {
        return new VideoId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    @JsonValue  // ← AÑADIR
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoId videoId = (VideoId) o;
        return Objects.equals(value, videoId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
