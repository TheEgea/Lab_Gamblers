package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import org.springframework.stereotype.Component;

@Component
public class VideoEntityMapper {

    /**
     * Convert JPA Entity to Domain Model
     */
    public Video toDomain(VideoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Video(
            new VideoId(entity.getId()),
            entity.getTitle(),
            entity.getDescription(),
            entity.getDurationSeconds(),
            entity.getSizeBytes(),
            entity.getMediaPath(),
            entity.getThumbnailPath(),
            entity.getChecksum(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    /**
     * Convert Domain Model to JPA Entity
     */
    public VideoEntity toEntity(Video video) {
        if (video == null) {
            return null;
        }

        return new VideoEntity(
            video.id().value(),
            video.title(),
            video.description(),
            video.durationSeconds(),
            video.sizeBytes(),
            video.mediaPath(),
            video.thumbnailPath(),
            video.checksum(),
            video.createdAt(),
            video.updatedAt()
        );
    }

    /**
     * Update existing entity with domain data
     */
    public void updateEntity(VideoEntity entity, Video video) {
        if (entity == null || video == null) {
            return;
        }

        entity.setTitle(video.title());
        entity.setDescription(video.description());
        entity.setDurationSeconds(video.durationSeconds());
        entity.setSizeBytes(video.sizeBytes());
        entity.setMediaPath(video.mediaPath());
        entity.setThumbnailPath(video.thumbnailPath());
        entity.setChecksum(video.checksum());
        entity.setUpdatedAt(video.updatedAt());
        // Note: ID and createdAt should never be updated
    }
}
