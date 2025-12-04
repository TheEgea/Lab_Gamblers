package com.tecnocampus.LS2.protube_back.persistance.jpa.video;

import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoMediaEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

    public class VideoMediaEntityTest {

        @Test
        void testSettersAndGetters() {
            VideoMediaEntity videoMediaEntity = new VideoMediaEntity();

            // Set values
            UUID id = UUID.randomUUID();
            UUID videoId = UUID.randomUUID();
            byte[] mediaBytes = {1, 2, 3};
            byte[] thumbnailBytes = {4, 5, 6};
            int width = 1920;
            int height = 1080;
            int durationSeconds = 300;

            videoMediaEntity.setId(id);
            videoMediaEntity.setVideoId(videoId);
            videoMediaEntity.setMediaBytes(mediaBytes);
            videoMediaEntity.setThumbnailBytes(thumbnailBytes);
            videoMediaEntity.setWidth(width);
            videoMediaEntity.setHeight(height);
            videoMediaEntity.setDurationSeconds(durationSeconds);

            // Assert values
            assertEquals(id, videoMediaEntity.getId());
            assertEquals(videoId, videoMediaEntity.getVideoId());
            assertArrayEquals(mediaBytes, videoMediaEntity.getMediaBytes());
            assertArrayEquals(thumbnailBytes, videoMediaEntity.getThumbnailBytes());
            assertEquals(width, videoMediaEntity.getWidth());
            assertEquals(height, videoMediaEntity.getHeight());
            assertEquals(durationSeconds, videoMediaEntity.getDurationSeconds());
        }



    }
