package com.tecnocampus.LS2.protube_back.domain.video;

import java.util.Objects;
import java.util.UUID;

public record VideoId(UUID value) {
    public VideoId {
        Objects.requireNonNull(value, "VideoId value cannot be null");
    }
    public static VideoId random() { return new VideoId(UUID.randomUUID()); }
    @Override public String toString() { return value.toString(); }
    public boolean equals (Object o) {
        if (!(o instanceof VideoId)) return false;
        return ((VideoId) o).value.equals(this.value);
    }
}