package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "videos",
        indexes = {
                @Index(name = "idx_videos_title", columnList = "title"),
                @Index(name = "idx_videos_created_at", columnList = "created_at")
        }
)
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

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }
    public void setMediaPath(String mediaPath) { this.mediaPath = mediaPath; }
    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }
    public void setChecksum(String checksum) { this.checksum = checksum; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    public void onCreate() {
        Instant now = Instant.now();
        if (this.createdAt == null)
            this.createdAt = now;
        if (this.updatedAt == null)
            this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoEntity that = (VideoEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "VideoEntity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", mediaPath='" + mediaPath + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
