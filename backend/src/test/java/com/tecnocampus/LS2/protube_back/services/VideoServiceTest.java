package com.tecnocampus.LS2.protube_back.services;
/*
import com.tecnocampus.LS2.protube_back.application.video.VideoService;
import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.domain.video.atributes.Comentario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
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

    /*@Test
    void existsByChecksum_ShouldReturnTrueIfChecksumExists() {
        Video video = createSampleVideo("1");
        videoService.save(video);

        assertTrue(videoService.existsByChecksum("checksum1"));
    }

    @Test
    void existsByChecksum_ShouldReturnFalseIfChecksumNotExists() {
        assertFalse(videoService.existsByChecksum("checksum1"));
    }

     */

    // Helper method to create a sample video
    private Video createSampleVideo(String id) {
        return new Video(
                new VideoId(id),
                "jsonId",
                1920,
                1080,
                300,
                "Sample Title",
                "Sample User",
                List.of(Instant.now()),
                "Sample Description",
                List.of("Category1", "Category2"),
                List.of("Tag1", "Tag2"),
                1000,
                100,
                "Sample Channel",
                List.of(new Comentario("User1", "Great video!",Instant.now(),0)),
                "media/path/sample.mp4",
                "thumbnails/sample.jpg",
                Instant.now(),
                Instant.now()
        );
    }
}