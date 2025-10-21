package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.application.video.VideoService;
import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VideoServiceTest {

    private VideoService videoService;

    @BeforeEach
    void setUp() {
        videoService = new VideoService();
    }

    @Test
    void listAll_ShouldReturnEmptyListInitially() {
        assertTrue(videoService.listAll().isEmpty());
    }

    @Test
    void save_ShouldAddVideoToList() {
        Video video = createSampleVideo("1");
        videoService.save(video);

        assertEquals(1, videoService.listAll().size());
        assertEquals(video, videoService.listAll().get(0));
    }

    @Test
    void findById_ShouldReturnVideoIfExists() {
        Video video = createSampleVideo("1");
        videoService.save(video);

        Optional<Video> foundVideo = videoService.findById(new VideoId("1"));
        assertTrue(foundVideo.isPresent());
        assertEquals(video, foundVideo.get());
    }

    @Test
    void findById_ShouldReturnEmptyIfNotExists() {
        Optional<Video> foundVideo = videoService.findById(new VideoId("1"));
        assertTrue(foundVideo.isEmpty());
    }

    @Test
    void delete_ShouldRemoveVideoFromList() {
        Video video = createSampleVideo("1");
        videoService.save(video);

        videoService.delete(new VideoId("1"));
        assertTrue(videoService.listAll().isEmpty());
    }

    @Test
    void existsByChecksum_ShouldReturnTrueIfChecksumExists() {
        Video video = createSampleVideo("1");
        videoService.save(video);

        assertTrue(videoService.existsByChecksum("checksum1"));
    }

    @Test
    void existsByChecksum_ShouldReturnFalseIfChecksumNotExists() {
        assertFalse(videoService.existsByChecksum("checksum1"));
    }

    // Helper method to create a sample video
    private Video createSampleVideo(String id) {
        return new Video(
                new VideoId(id),
                "Sample Title",
                "Sample Description",
                120,
                1024L,
                "/media/sample.mp4",
                "/thumbnails/sample.jpg",
                "checksum1",
                Instant.now(),
                Instant.now()
        );
    }
}