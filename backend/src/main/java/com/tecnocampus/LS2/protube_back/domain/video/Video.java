package com.tecnocampus.LS2.protube_back.domain.video;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "videos")
public final class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final VideoId id;
    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR(255)")
    private final String title;
    @Column(name = "description", nullable = true, columnDefinition = "VARCHAR(255)")
    private final String description;
    @Column(name = "duration_seconds", nullable = true, columnDefinition = "INTEGER")
    private final Integer durationSeconds; // null si desconocido
    @Column(name = "size_bytes", nullable = true, columnDefinition = "BIGINT")
    private final Long sizeBytes;          // null si desconocido
    @Column(name = "media_path", nullable = false, columnDefinition = "VARCHAR(255)")
    private final String mediaPath;        // ruta relativa/clave de almacenamiento
    @Column(name = "thumbnail_path", nullable = true, columnDefinition = "VARCHAR(255)")
    private final String thumbnailPath;    // opcional
    @Column(name = "checksum", nullable = false, columnDefinition = "VARCHAR(64)")
    private final String checksum;         // para deduplicar
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private final Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
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