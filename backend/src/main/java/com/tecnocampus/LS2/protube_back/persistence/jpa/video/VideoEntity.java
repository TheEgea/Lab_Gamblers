package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "videos")
public class VideoEntity {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "title", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration", nullable = true, columnDefinition = "INTEGER")
    private Integer durationSeconds;

    @Column(name = "size", nullable = true, columnDefinition = "BIGINT")
    private Long sizeBytes;

    @Column(name = "media_path", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String mediaPath;

    @Column(name = "thumbnail_path", nullable = true, unique = false, columnDefinition = "VARCHAR(255)")
    private String thumbnailPath;

    @Column(name = "checksum", nullable = false, unique = true, columnDefinition = "VARCHAR(64)")
    private String checksum;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    protected VideoEntity() {}

    public VideoEntity(UUID id, String title, String description,
                       Integer durationSeconds, Long sizeBytes,
                       String mediaPath, String thumbnailPath,
                       String checksum, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.durationSeconds = durationSeconds;
        this.sizeBytes = sizeBytes;
        this.mediaPath = mediaPath;
        this.thumbnailPath = thumbnailPath;
        this.checksum = checksum;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public Long getSizeBytes() { return sizeBytes; }
    public String getMediaPath() { return mediaPath; }
    public String getThumbnailPath() { return thumbnailPath; }
    public String getChecksum() { return checksum; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public VideoEntity withTitle(String newTitle) {
        return new VideoEntity(id, newTitle, description, durationSeconds, sizeBytes,
                mediaPath, thumbnailPath, checksum, createdAt, Instant.now());
    }



}
