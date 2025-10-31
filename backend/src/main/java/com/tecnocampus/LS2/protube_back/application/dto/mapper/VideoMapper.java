package com.tecnocampus.LS2.protube_back.application.dto.mapper;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.web.dto.response.VideoResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class VideoMapper {

    public static VideoResponse toResponse(Video video) {
        if (video == null) return null;

        return new VideoResponse(
                video.getId().value(),
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

    public Video toDomain(CreateVideoRequest request) {
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

    private static String formatDuration(Integer seconds) {
        if (seconds == null || seconds <= 0) return "00:00";

        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private static String formatFileSize(Long bytes) {
        if (bytes == null || bytes <= 0) return null;
        return String.format("%.1f MB", bytes / 1024.0 / 1024.0);
    }

    private static String buildMediaUrl(String path) {
        return path != null ? "/media/videos/" + path : null;
    }

    private static String buildThumbnailUrl(String path) {
        return path != null ? "/media/thumbnails/" + path : null;
    }
}
