package com.tecnocampus.LS2.protube_back.domain.video;

import com.tecnocampus.LS2.protube_back.domain.user.User;
import com.tecnocampus.LS2.protube_back.domain.video.atributes.Comentario;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;


public final class Video {
    private final VideoId id;
    private final String jsonId;
    private final int width;
    private final int height;
    private final Integer durationSeconds; // null si desconocido
    private final String title;
    private final String user;
    private final List<Instant> timestamp;
    private final String description;
    private final List<String> categories;
    private final List<String>  tags;
    private final int viewCount;
    private final int likeCount;
    private final String channel;
    private final List<Comentario>  comments;
    private final String mediaPath;        // ruta relativa/clave de almacenamiento
    private final String thumbnailPath;    // opcional
    private final Instant createdAt;
    private final Instant updatedAt;

    public Video(VideoId id, String jsonId, int width, int height, Integer durationSeconds,
                 String title, String user, List<Instant>  timestamp, String description, List<String>  categories,
                 List<String>  tags, int viewCount, int likeCount, String channel, List<Comentario>  comments,
                 String mediaPath, String thumbnailPath, Instant createdAt, Instant updatedAt) {
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

    public Video(VideoId id) {
        this.id = id;
        this.jsonId = "";
        this.width = 0;
        this.height = 0;
        this.durationSeconds = null;
        this.title = "";
        this.user = "";
        this.timestamp = List.of();
        this.description = "";
        this.categories = List.of();
        this.tags = List.of();
        this.viewCount = 0;
        this.likeCount = 0;
        this.channel = "";
        this.comments = List.of();
        this.mediaPath = "";
        this.thumbnailPath = "";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public VideoId getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public String getJsonId() {
        return jsonId;
    }

    public int getHeight() {
        return height;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public List<Instant>  getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public List<String>  getCategories() {
        return categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getChannel() {
        return channel;
    }

    public List<Comentario>  getComments() {
        return comments;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Video withTitle(String newTitle) {
        return new Video(
                this.id,
                this.jsonId,
                this.width,
                this.height,
                this.durationSeconds,
                newTitle,
                this.user,
                this.timestamp,
                this.description,
                this.categories,
                this.tags,
                this.viewCount,
                this.likeCount,
                this.channel,
                this.comments,
                this.mediaPath,
                this.thumbnailPath,
                this.createdAt,
                this.updatedAt
        );
    }

    public boolean equals(Object o) {
        if (!(o instanceof Video)) return false;
        return ((Video) o).id.equals(this.id);
    }
}