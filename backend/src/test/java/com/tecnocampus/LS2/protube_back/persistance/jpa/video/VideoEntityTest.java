package com.tecnocampus.LS2.protube_back.persistance.jpa.video;

import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VideoEntityTest {

    @Test
    void testVideoEntityProperties() {
        // Valores de prueba
        String id = "video123";
        String jsonId = "json123";
        int width = 1920;
        int height = 1080;
        Integer durationSeconds = 300;
        String title = "Test Video";
        String user = "testUser";
        List<Instant> timestamps = List.of(Instant.now());
        String description = "Test Description";
        List<String> categories = List.of("Category1", "Category2");
        List<String> tags = List.of("Tag1", "Tag2");
        int viewCount = 1000;
        int likeCount = 500;
        String channel = "Test Channel";
        String mediaPath = "/path/to/media";
        String thumbnailPath = "/path/to/thumbnail";
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        VideoEntity videoEntity = new VideoEntity();

        videoEntity.setId(id);
        videoEntity.setJsonId(jsonId);
        videoEntity.setWidth(width);
        videoEntity.setHeight(height);
        videoEntity.setDurationSeconds(durationSeconds);
        videoEntity.setTitle(title);
        videoEntity.setUser(user);
        videoEntity.setTimestamp(timestamps);
        videoEntity.setDescription(description);
        videoEntity.setCategories(categories);
        videoEntity.setTags(tags);
        videoEntity.setViewCount(viewCount);
        videoEntity.setLikeCount(likeCount);
        videoEntity.setChannel(channel);
        videoEntity.setMediaPath(mediaPath);
        videoEntity.setThumbnailPath(thumbnailPath);
        videoEntity.setCreatedAt(createdAt);
        videoEntity.setUpdatedAt(updatedAt);

        assertEquals(id, videoEntity.getId());
        assertEquals(jsonId, videoEntity.getJsonId());
        assertEquals(width, videoEntity.getWidth());
        assertEquals(height, videoEntity.getHeight());
        assertEquals(durationSeconds, videoEntity.getDurationSeconds());
        assertEquals(title, videoEntity.getTitle());
        assertEquals(user, videoEntity.getUser());
        assertEquals(timestamps, videoEntity.getTimestamp());
        assertEquals(description, videoEntity.getDescription());
        assertEquals(categories, videoEntity.getCategories());
        assertEquals(tags, videoEntity.getTags());
        assertEquals(viewCount, videoEntity.getViewCount());
        assertEquals(likeCount, videoEntity.getLikeCount());
        assertEquals(channel, videoEntity.getChannel());
        assertEquals(mediaPath, videoEntity.getMediaPath());
        assertEquals(thumbnailPath, videoEntity.getThumbnailPath());
        assertEquals(createdAt, videoEntity.getCreatedAt());
        assertEquals(updatedAt, videoEntity.getUpdatedAt());
    }

    @Test
    void testDefaultConstructor() {
        VideoEntity videoEntity = new VideoEntity();
        assertNotNull(videoEntity);
        assertNull(videoEntity.getId());
    }

}