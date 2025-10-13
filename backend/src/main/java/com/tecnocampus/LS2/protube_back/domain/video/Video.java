package com.tecnocampus.LS2.protube_back.domain.video;

import java.time.Instant;
import java.util.Objects;

public final class Video {
    private final VideoId id;
    private final String title;
    private final String description;
    private final Integer durationSeconds; // null si desconocido
    private final Long sizeBytes;          // null si desconocido
    private final String mediaPath;        // ruta relativa/clave de almacenamiento
    private final String thumbnailPath;    // opcional
    private final String checksum;         // para deduplicar
    private final Instant createdAt;
    private final Instant updatedAt;

    public Video(VideoId id, String title, String description,
                 Integer durationSeconds, Long sizeBytes,
                 String mediaPath, String thumbnailPath,
                 String checksum, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNullElse(title, "");
        this.description = description;
        this.durationSeconds = durationSeconds;
        this.sizeBytes = sizeBytes;
        this.mediaPath = Objects.requireNonNull(mediaPath);
        this.thumbnailPath = thumbnailPath;
        this.checksum = Objects.requireNonNull(checksum);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public VideoId id() { return id; }
    public String title() { return title; }
    public String description() { return description; }
    public Integer durationSeconds() { return durationSeconds; }
    public Long sizeBytes() { return sizeBytes; }
    public String mediaPath() { return mediaPath; }
    public String thumbnailPath() { return thumbnailPath; }
    public String checksum() { return checksum; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }

    public Video withTitle(String newTitle) {
        return new Video(id, newTitle, description, durationSeconds, sizeBytes,
                mediaPath, thumbnailPath, checksum, createdAt, Instant.now());
    }
}