package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "videos")
public class VideoEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "media_path", nullable = false, length = 500)
    private String mediaPath;

    @Column(name = "thumbnail_path", length = 500)
    private String thumbnailPath;

    @Column(name = "checksum", nullable = false, unique = true, length = 64)
    private String checksum;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Default constructor (required by JPA)
    public VideoEntity() {}

    // Full constructor
    public VideoEntity(String id, String title, String description,
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

    // Getters
    public String getId() { return id; }
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
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }
    public void setMediaPath(String mediaPath) { this.mediaPath = mediaPath; }
    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }
    public void setChecksum(String checksum) { this.checksum = checksum; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

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
