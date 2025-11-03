package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.api.VideoController;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.VideoMapper;
import com.tecnocampus.LS2.protube_back.application.dto.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.application.dto.request.UpdateVideoRequest;
import com.tecnocampus.LS2.protube_back.application.dto.response.VideoResponse;
import com.tecnocampus.LS2.protube_back.application.video.VideoService;
import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideosControllerTest {

    @Mock
    private VideoService videoService;

    @InjectMocks
    private VideoController videoController;


    @Test
    void testGetAllVideos() {
        Video video = createSampleVideo(UUID.randomUUID().toString());
        when(videoService.listAll()).thenReturn(List.of(video));

        ResponseEntity<List<Video>> response = videoController.getAllVideos();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(List.of(video), response.getBody());
    }

    @Test
    void testAddVideo() {
        CreateVideoRequest request = createSampleCreateVideoRequest(createSampleVideo(UUID.randomUUID().toString()).getId().toString());
        doNothing().when(videoService).save(request);
        ResponseEntity<String> response = videoController.addVideo(request);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Video created successfully", response.getBody());
    }

    @Test
    void testFindById() {
        String id = UUID.randomUUID().toString().toString();
        VideoResponse responseMock = createSampleVideoResponse(createSampleVideo(id));
        when(videoService.findById(id)).thenReturn(responseMock);

        ResponseEntity<VideoResponse> response = videoController.findById(id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseMock, response.getBody());
    }

    @Test
    void testGetRandomVideo() {
        Video video = createSampleVideo(UUID.randomUUID().toString());
        when(videoService.getRandomVideo()).thenReturn(Optional.of(video));

        ResponseEntity<Video> response = videoController.getRandomVideo();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(video, response.getBody());
    }

    private Video createSampleVideo(String id) {
        return new Video(
                new VideoId(id),
                "jsonId",
                1920,
                1080,
                300,
                "Sample Title",
                "Sample User",
                List.of(Instant.parse("2023-01-01T00:00:00Z")),
                "Sample Description",
                List.of("Category1", "Category2"),
                List.of("Tag1", "Tag2"),
                1000,
                100,
                "Sample Channel",
                List.of(),
                "media/path",
                "thumbnail/path",
                Instant.parse("2023-01-01T00:00:00Z"),
                Instant.parse("2023-01-01T00:00:00Z")
        );
    }

    private VideoEntity createSampleVideoEntity(Video video) {
        return VideoEntityMapper.toEntity(video);
    }

    private CreateVideoRequest createSampleCreateVideoRequest(String id) {
        return new CreateVideoRequest(
                "Sample Title",
                "Sample Description",
                id,
                "jsonId",
                1920,
                1080,
                300,
                "Sample User",
                List.of(Instant.parse("2023-01-01T00:00:00Z")),
                List.of("Category1", "Category2"),
                List.of("Tag1", "Tag2"),
                1000,
                100,
                "Sample Channel",
                List.of(),
                "media/path",
                "thumbnail/path"
        );
    }

    private VideoResponse createSampleVideoResponse(Video video) {
        return VideoMapper.toResponse(video);
    }

    private UpdateVideoRequest createSampleUpdateVideoRequest() {
        return new UpdateVideoRequest(
                "Updated Sample Title",
                "Updated Sample Description"
        );
    }

}