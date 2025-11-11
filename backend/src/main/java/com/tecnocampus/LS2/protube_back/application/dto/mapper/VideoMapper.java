package com.tecnocampus.LS2.protube_back.application.dto.mapper;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.application.dto.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.application.dto.response.VideoResponse;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoProjection;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class VideoMapper {

    public static VideoResponse toResponse(Video video) {
        if (video == null) return null;

        return new VideoResponse(
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
                video.getComments(),
                buildMediaUrl(video.getMediaPath()),
                buildThumbnailUrl(video.getThumbnailPath()),
                video.getCreatedAt(),
                video.getUpdatedAt()
        );
    }

    public static Video toDomain(CreateVideoRequest request) {
        if (request == null) return null;

        Instant now = Instant.now();

        return new Video(
                VideoId.generate(), // New ID will be generated
                request.jsonId(),
                request.width(),
                request.height(),
                request.durationSeconds(),
                request.title(),
                request.user(),
                request.timestamp(),
                request.description(),
                request.categories(),
                request.tags(),
                0, // Initial view count
                0, // Initial like count
                request.channel(),
                request.comments(), // Initial empty comments
                request.mediaPath(),
                request.thumbnailPath(),
                now,
                now
        );
    }

    private static String buildMediaUrl(String path) {
        return path != null ? "/media/videos/" + path : null;
    }

    private static String buildThumbnailUrl(String path) {
        return path != null ? "/media/thumbnails/" + path : null;
    }

    public static Video VideoProjectionToDomain(VideoProjection projection) {
        if (projection == null) return null;
        return new Video(
                new VideoId(projection.getId()),
                projection.getJsonId(),
                projection.getWidth(),
                projection.getHeight(),
                projection.getDurationSeconds(),
                projection.getTitle(),
                projection.getUser(),
                List.of(), // No timestamp info in projection
                projection.getDescription(),
                List.of(), // No categories info in projection
                List.of(), // No tags info in projection
                projection.getViewCount(),
                projection.getLikeCount(),
                projection.getChannel(),
                List.of(), // No comments info in projection
                projection.getMediaPath(),
                projection.getThumbnailPath(),
                projection.getCreatedAt(),
                projection.getUpdatedAt()
        );
    }



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
                CommentMapper.toDomainList(entity.getComments()),
                entity.getMediaPath(),
                entity.getThumbnailPath(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

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
                CommentMapper.toEntityList(video.getComments()),
                video.getMediaPath(),
                video.getThumbnailPath(),
                video.getCreatedAt(),
                video.getUpdatedAt()
        );
    }
}
