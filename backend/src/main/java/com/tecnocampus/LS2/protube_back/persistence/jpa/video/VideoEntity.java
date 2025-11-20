package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import com.tecnocampus.LS2.protube_back.persistence.jpa.video.atributes.ComentarioEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "videos")
@Data
public class VideoEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "json_id", length = 10)
    private String jsonId;
    @Column(name = "width")
    private int width;
    @Column(name = "height")
    private int height;
    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "username", length = 64)
    private String user;

    @ElementCollection
    @CollectionTable(name = "video_timestamps", joinColumns = @JoinColumn(name = "video_id"))
    @Column(name = "timestamp")
    private List<Instant> timestamp;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "video_categories", joinColumns = @JoinColumn(name = "video_id"))
    @Column(name = "category")
    private List<String> categories;

    @ElementCollection
    @CollectionTable(name = "video_tags", joinColumns = @JoinColumn(name = "video_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "view_count")
    private int viewCount;
    @Column(name = "like_count")
    private int likeCount;
    @Column(name = "channel", length = 64)
    private String channel;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "video_id")
    private List<ComentarioEntity> comments;

    @Column(name = "media_path", nullable = false, length = 500)
    private String mediaPath;

    @Column(name = "thumbnail_path", length = 500)
    private String thumbnailPath;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "video_bytes")
    private byte[] videoBytes;

    @Column(name = "video_size")
    private Long videoSize;

    @Column(name = "video_mime", length = 64)
    private String videoMime = "video/mp4";

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "thumbnail_bytes")
    private byte[] thumbnailBytes;

    @Column(name = "thumbnail_size")
    private Long thumbnailSize;

    @Column(name = "thumbnail_mime", length = 64)
    private String thumbnailMime = "image/webp";

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Default constructor (required by JPA)
    public VideoEntity() {}


    public VideoEntity(String id, String jsonId, int width, int height, Integer durationSeconds,
                       String title, String user, List<Instant> timestamp, String description,
                       List<String> categories, List<String>  tags, int viewCount, int likeCount,
                       String channel, List<ComentarioEntity> comments, String mediaPath,
                       String thumbnailPath, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.jsonId = jsonId;
        this.width = width;
        this.height = height;
        this.durationSeconds = durationSeconds;
        this.title = title;
        this.user = user;
        this.timestamp = timestamp;
        this.description = description;
        this.categories = categories;
        this.tags = tags;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.channel = channel;
        this.comments = comments;
        this.mediaPath = mediaPath;
        this.thumbnailPath = thumbnailPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonId() {
        return jsonId;
    }

    public void setJsonId(String jsonId) {
        this.jsonId = jsonId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Instant> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(List<Instant> timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<ComentarioEntity> getComments() {
        return comments;
    }

    public void setComments(List<ComentarioEntity> comments) {
        this.comments = comments;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public byte[] getVideoBytes() {
        return videoBytes;
    }

    public void setVideoBytes(byte[] videoBytes) {
        this.videoBytes = videoBytes;
    }

    public Long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(Long videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoMime() {
        return videoMime;
    }

    public void setVideoMime(String videoMime) {
        this.videoMime = videoMime;
    }

    public byte[] getThumbnailBytes() {
        return thumbnailBytes;
    }

    public void setThumbnailBytes(byte[] thumbnailBytes) {
        this.thumbnailBytes = thumbnailBytes;
    }

    public Long getThumbnailSize() {
        return thumbnailSize;
    }

    public void setThumbnailSize(Long thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
    }

    public String getThumbnailMime() {
        return thumbnailMime;
    }

    public void setThumbnailMime(String thumbnailMime) {
        this.thumbnailMime = thumbnailMime;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
