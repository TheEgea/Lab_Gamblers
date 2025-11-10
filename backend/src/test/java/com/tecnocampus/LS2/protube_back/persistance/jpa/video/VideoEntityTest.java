package com.tecnocampus.LS2.protube_back.persistance.jpa.video;

import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.atributes.ComentarioEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VideoEntityTest {

    @Test
    void testConstructorValues_IdAndJsonId() {
        String id = "video123";
        String jsonId = "json123";

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setId(id);
        videoEntity.setJsonId(jsonId);

        assertEquals(id, videoEntity.getId());
        assertEquals(jsonId, videoEntity.getJsonId());
    }

    @Test
    void testConstructorValues_DimensionsAndDuration() {
        int width = 1920;
        int height = 1080;
        Integer durationSeconds = 300;

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setWidth(width);
        videoEntity.setHeight(height);
        videoEntity.setDurationSeconds(durationSeconds);

        assertEquals(width, videoEntity.getWidth());
        assertEquals(height, videoEntity.getHeight());
        assertEquals(durationSeconds, videoEntity.getDurationSeconds());
    }

    @Test
    void testConstructorValues_TitleAndUser() {
        String title = "Test Video";
        String user = "testUser";

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setTitle(title);
        videoEntity.setUser(user);

        assertEquals(title, videoEntity.getTitle());
        assertEquals(user, videoEntity.getUser());
    }

    @Test
    void testConstructorValues_TimestampsAndDescription() {
        List<Instant> timestamps = List.of(Instant.now());
        String description = "Test Description";

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setTimestamp(timestamps);
        videoEntity.setDescription(description);

        assertEquals(timestamps, videoEntity.getTimestamp());
        assertEquals(description, videoEntity.getDescription());
    }

    @Test
    void testConstructorValues_CategoriesAndTags() {
        List<String> categories = List.of("Category1", "Category2");
        List<String> tags = List.of("Tag1", "Tag2");

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setCategories(categories);
        videoEntity.setTags(tags);

        assertEquals(categories, videoEntity.getCategories());
        assertEquals(tags, videoEntity.getTags());
    }

    @Test
    void testConstructorValues_ViewAndLikeCounts() {
        int viewCount = 1000;
        int likeCount = 500;

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setViewCount(viewCount);
        videoEntity.setLikeCount(likeCount);

        assertEquals(viewCount, videoEntity.getViewCount());
        assertEquals(likeCount, videoEntity.getLikeCount());
    }

    @Test
    void testConstructorValues_ChannelAndPaths() {
        String channel = "Test Channel";
        String mediaPath = "/path/to/media";
        String thumbnailPath = "/path/to/thumbnail";

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setChannel(channel);
        videoEntity.setMediaPath(mediaPath);
        videoEntity.setThumbnailPath(thumbnailPath);

        assertEquals(channel, videoEntity.getChannel());
        assertEquals(mediaPath, videoEntity.getMediaPath());
        assertEquals(thumbnailPath, videoEntity.getThumbnailPath());
    }

    @Test
    void testConstructorValues_CreatedAndUpdatedAt() {
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setCreatedAt(createdAt);
        videoEntity.setUpdatedAt(updatedAt);

        assertEquals(createdAt, videoEntity.getCreatedAt());
        assertEquals(updatedAt, videoEntity.getUpdatedAt());
    }
}