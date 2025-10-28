package com.tecnocampus.LS2.protube_back.application.video;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoCatalogPort;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.web.dto.mapper.VideoMapper;
import com.tecnocampus.LS2.protube_back.web.dto.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.web.dto.request.UpdateVideoRequest;
import com.tecnocampus.LS2.protube_back.web.dto.response.VideoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tecnocampus.LS2.protube_back.exception.video.VideoNotFoundException;


import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class VideoApplicationService {

    private final VideoCatalogPort videoCatalogPort;
    private final VideoMapper videoMapper;

    public VideoApplicationService(VideoCatalogPort videoCatalogPort, VideoMapper videoMapper) {
        this.videoCatalogPort = videoCatalogPort;
        this.videoMapper = videoMapper;
    }

    /**
     * Create a new video
     */
    public VideoResponse createVideo(CreateVideoRequest request) {
        Video video = videoMapper.toDomain(request);
        videoCatalogPort.save(video);
        return videoMapper.toResponse(video);
    }

    /**
     * Get all videos with pagination
     */
    @Transactional(readOnly = true)
    public Page<VideoResponse> getAllVideos(Pageable pageable) {
        // For now, we'll get all and paginate manually
        // Later we can optimize this with proper repository pagination
        List<Video> allVideos = videoCatalogPort.listAll();

        List<VideoResponse> videoResponses = allVideos.stream()
                .map(videoMapper::toResponse)
                .toList();

        // Manual pagination (not optimal, but works for now)
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), videoResponses.size());

        List<VideoResponse> pageContent = start < videoResponses.size() ?
                videoResponses.subList(start, end) : List.of();

        return new PageImpl<>(pageContent, pageable, videoResponses.size());
    }

    /**
     * Get all videos as simple list (no pagination)
     */
    @Transactional(readOnly = true)
    public List<VideoResponse> getAllVideos() {
        return videoCatalogPort.listAll().stream()
                .map(videoMapper::toResponse)
                .toList();
    }

    /**
     * Get video by ID
     */
    @Transactional(readOnly = true)
    public VideoResponse getVideoById(String id) {
        VideoId videoId = new VideoId(id);
        Video video = videoCatalogPort.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + id));

        return videoMapper.toResponse(video);
    }

    /**
     * Update video
     */
    public VideoResponse updateVideo(String id, UpdateVideoRequest request) {
        VideoId videoId = new VideoId(id);
        Video existingVideo = videoCatalogPort.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + id));

        // Create updated video
        Video updatedVideo = updateVideoFromRequest(existingVideo, request);
        videoCatalogPort.save(updatedVideo);

        return videoMapper.toResponse(updatedVideo);
    }

    /**
     * Delete video
     */
    public void deleteVideo(String id) {
        VideoId videoId = new VideoId(id);

        // Check if video exists
        if (!videoCatalogPort.findById(videoId).isPresent()) {
            throw new VideoNotFoundException("Video not found with id: " + id);
        }

        videoCatalogPort.delete(videoId);
    }

    @Transactional(readOnly = true)
    public long getTotalVideoCount() {
        return videoCatalogPort.listAll().size();
    }

    // Private helper methods

    private Video updateVideoFromRequest(Video existingVideo, UpdateVideoRequest request) {
        String newTitle = request.title() != null ? request.title() : existingVideo.getTitle();
        String newDescription = request.description() != null ? request.description() : existingVideo.getDescription();

        return new Video(
                existingVideo.getId(),
                existingVideo.getJsonId(),
                existingVideo.getWidth(),
                existingVideo.getHeight(),
                existingVideo.getDurationSeconds(),
                newTitle,
                existingVideo.getUser(),
                existingVideo.getTimestamp(),
                newDescription,
                existingVideo.getCategories(),
                existingVideo.getTags(),
                existingVideo.getViewCount(),
                existingVideo.getLikeCount(),
                existingVideo.getChannel(),
                existingVideo.getComments(),
                existingVideo.getMediaPath(),
                existingVideo.getThumbnailPath(),
                existingVideo.getCreatedAt(),
                Instant.now() // updatedAt
        );
    }

    /**
     * Endpoint that returns a random video from the catalog
     */
    @Transactional(readOnly = true)
    public VideoResponse getRandomVideo() {
        Video video = videoCatalogPort.getRandomVideo()
                .orElseThrow(() -> new VideoNotFoundException("No videos available"));
        return videoMapper.toResponse(video);

    }
