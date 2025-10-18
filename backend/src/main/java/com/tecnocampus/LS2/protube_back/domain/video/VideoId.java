package com.tecnocampus.LS2.protube_back.domain.video;

import java.util.Objects;
import java.util.UUID;

public final class VideoId {
    private final String value;

    public VideoId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static VideoId generate() {
        return new VideoId(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
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

    @Override
    public String toString() {
        return "VideoId{" + value + '}';
    }
}
