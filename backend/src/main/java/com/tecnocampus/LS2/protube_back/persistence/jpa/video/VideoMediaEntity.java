package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "video_media")
public class VideoMediaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "video_id", nullable = false, unique = true)
    private UUID videoId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "media_bytes", nullable = false)
    private byte[] mediaBytes;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "thumbnail_bytes")
    private byte[] thumbnailBytes;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    // getters y setters

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getVideoId() { return videoId; }
    public void setVideoId(UUID videoId) { this.videoId = videoId; }

    public byte[] getMediaBytes() { return mediaBytes; }
    public void setMediaBytes(byte[] mediaBytes) { this.mediaBytes = mediaBytes; }

    public byte[] getThumbnailBytes() { return thumbnailBytes; }
    public void setThumbnailBytes(byte[] thumbnailBytes) { this.thumbnailBytes = thumbnailBytes; }

    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }

    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }

    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
}
