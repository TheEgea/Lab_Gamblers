package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VideoServiceTest {

    static VideoService videoService = new VideoService();
    static Video video1, video2;


    @BeforeAll
    static void setup() {
        VideoId videoId1 = VideoId.random();
        video1 = new Video(videoId1, "video1", "description1", null, null, "path1", "paththumbnail1", "checksum1", Instant.now(), Instant.now());

        videoService.save(video1);

        VideoId videoId2 = VideoId.random();
        video2 = new Video(videoId2, "video2", "description2", null, null, "path2", "paththumbnail2", "checksum2", Instant.now(), Instant.now());

        videoService.save(video2);
    }

    @Test
    void checkVideoExistance() {
        if (videoService.equals(List.of(video1, video2))) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    @Test
    void checkVideoAdds() {
        VideoId videoId3 = VideoId.random();
        Video video3 = new Video(videoId3, "video3", "description3", null, null, "path3", "paththumbnail3", "checksum3", Instant.now(), Instant.now());

        videoService.save(video3);

        if (videoService.equals(List.of(video1, video2,video3))) {
            videoService.delete(videoId3);
            assertTrue(true);
        } else {
            fail();
        }
    }
    @Test
    void checkVideoDeletes() {
        VideoId videoId3 = VideoId.random();
        Video video3 = new Video(videoId3, "video3", "description3", null, null, "path3", "paththumbnail3", "checksum3", Instant.now(), Instant.now());

        videoService.save(video3);
        videoService.delete(videoId3);
        if (videoService.equals(List.of(video1, video2))) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    @Test
    void checkVideoExistsByChecksum() {
        assertTrue(videoService.existsByChecksum("checksum1"));
        assertTrue(videoService.existsByChecksum("checksum2"));
        assertFalse(videoService.existsByChecksum("checksum3"));
    }

    @Test
    void checkVideoExistsById() {
        assertTrue(videoService.findById(video1.id()).isPresent());
        assertTrue(videoService.findById(video2.id()).isPresent());
        assertFalse(videoService.findById(VideoId.random()).isPresent());
    }

}