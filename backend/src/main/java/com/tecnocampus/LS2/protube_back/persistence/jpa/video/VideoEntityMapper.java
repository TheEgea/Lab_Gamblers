package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.atributes.ComentarioEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class VideoEntityMapper {

    /**
     * Convert JPA Entity to Domain Model
     */
    public static Video toDomain(VideoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Video(
                new VideoId(entity.getId()),
                entity.getJsonId(),
                entity.getWidth(),
                entity.getHeight(),
                entity.getDurationSeconds(),
                entity.getTitle(),
                entity.getUser(),
                entity.getTimestamp(),
                entity.getDescription(),
                entity.getCategories(),
                entity.getTags(),
                entity.getViewCount(),
                entity.getLikeCount(),
                entity.getChannel(),
                ComentarioEntityMapper.toDomainList(entity.getComments()),
                entity.getMediaPath(),
                entity.getThumbnailPath(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Convert Domain Model to JPA Entity
     */
    public static VideoEntity toEntity(Video video) {
        if (video == null) {
            return null;
        }

        return new VideoEntity(
                video.getId().value().toString(),
                video.getJsonId(),
                video.getWidth(),
                video.getHeight(),
                video.getDurationSeconds(),
                video.getTitle(),
                video.getUser(),
                video.getTimestamp(),
                video.getDescription(),
                video.getCategories(),
                video.getTags(),
                video.getViewCount(),
                video.getLikeCount(),
                video.getChannel(),
                ComentarioEntityMapper.toEntityList(video.getComments()),
                video.getMediaPath(),
                video.getThumbnailPath(),
                video.getCreatedAt(),
                video.getUpdatedAt()
        );
    }

    /**
     * Update existing entity with domain data
     */
    public static void updateEntity(VideoEntity entity, Video video) {
        if (entity == null || video == null) {
            return;
        }

        //TODO:

        entity.setWidth(video.getWidth());
        entity.setHeight(video.getHeight());
        entity.setDurationSeconds(video.getDurationSeconds());
        entity.setTitle(video.getTitle());
        entity.setUser(video.getUser());
        entity.setTimestamp(video.getTimestamp());
        entity.setDescription(video.getDescription());
        entity.setCategories(video.getCategories());
        entity.setTags(video.getTags());
        entity.setViewCount(video.getViewCount());
        entity.setLikeCount(video.getLikeCount());
        entity.setChannel(video.getChannel());
        entity.setComments(ComentarioEntityMapper.toEntityList(video.getComments()));
        entity.setMediaPath(video.getMediaPath());
        entity.setThumbnailPath(video.getThumbnailPath());
        entity.setUpdatedAt(video.getUpdatedAt());

        // Note: ID and createdAt should never be updated
    }
}