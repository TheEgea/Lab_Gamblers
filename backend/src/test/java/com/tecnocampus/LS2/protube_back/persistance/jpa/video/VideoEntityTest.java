package com.tecnocampus.LS2.protube_back.persistance.jpa.video;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


import com.tecnocampus.LS2.protube_back.persistence.jpa.video.atributes.ComentarioEntity;

public class VideoEntityTest {

    @Test
    void testSettersAndGetters() {
        VideoEntity videoEntity = new VideoEntity();

        // Set values
        videoEntity.setId("123");
        videoEntity.setJsonId("json123");
        videoEntity.setWidth(1920);
        videoEntity.setHeight(1080);
        videoEntity.setDurationSeconds(300);
        videoEntity.setTitle("Test Video");
        videoEntity.setUser("testUser");
        videoEntity.setTimestamp(List.of(Instant.now()));
        videoEntity.setDescription("Test Description");
        videoEntity.setCategories(List.of("Category1", "Category2"));
        videoEntity.setTags(List.of("Tag1", "Tag2"));
        videoEntity.setViewCount(1000);
        videoEntity.setLikeCount(500);
        videoEntity.setChannel("Test Channel");
        videoEntity.setComments(List.of(new ComentarioEntity("Test Comment", "Author", Instant.now(), 10)));
        videoEntity.setMediaPath("/path/to/media.mp4");
        videoEntity.setThumbnailPath("/path/to/thumbnail.webp");
        videoEntity.setVideoBytes(new byte[]{1, 2, 3});
        videoEntity.setVideoSize(1024L);
        videoEntity.setVideoMime("video/mp4");
        videoEntity.setThumbnailBytes(new byte[]{4, 5, 6});
        videoEntity.setThumbnailSize(512L);
        videoEntity.setThumbnailMime("image/webp");
        videoEntity.setCreatedAt(Instant.now());
        videoEntity.setUpdatedAt(Instant.now());

        // Assert values
        assertEquals("123", videoEntity.getId());
        assertEquals("json123", videoEntity.getJsonId());
        assertEquals(1920, videoEntity.getWidth());
        assertEquals(1080, videoEntity.getHeight());
        assertEquals(300, videoEntity.getDurationSeconds());
        assertEquals("Test Video", videoEntity.getTitle());
        assertEquals("testUser", videoEntity.getUser());
        assertNotNull(videoEntity.getTimestamp());
        assertEquals("Test Description", videoEntity.getDescription());
        assertEquals(List.of("Category1", "Category2"), videoEntity.getCategories());
        assertEquals(List.of("Tag1", "Tag2"), videoEntity.getTags());
        assertEquals(1000, videoEntity.getViewCount());
        assertEquals(500, videoEntity.getLikeCount());
        assertEquals("Test Channel", videoEntity.getChannel());
        assertNotNull(videoEntity.getComments());
        assertEquals("/path/to/media.mp4", videoEntity.getMediaPath());
        assertEquals("/path/to/thumbnail.webp", videoEntity.getThumbnailPath());
        assertArrayEquals(new byte[]{1, 2, 3}, videoEntity.getVideoBytes());
        assertEquals(1024L, videoEntity.getVideoSize());
        assertEquals("video/mp4", videoEntity.getVideoMime());
        assertArrayEquals(new byte[]{4, 5, 6}, videoEntity.getThumbnailBytes());
        assertEquals(512L, videoEntity.getThumbnailSize());
        assertEquals("image/webp", videoEntity.getThumbnailMime());
        assertNotNull(videoEntity.getCreatedAt());
        assertNotNull(videoEntity.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        VideoEntity video1 = new VideoEntity();
        video1.setId("123");

        VideoEntity video2 = new VideoEntity();
        video2.setId("123");

        assertEquals(video1, video2);
        assertEquals(video1.hashCode(), video2.hashCode());
    }

    @Test
    void testToString() {
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setId("123");
        videoEntity.setTitle("Test Video");
        videoEntity.setMediaPath("/path/to/media.mp4");
        videoEntity.setCreatedAt(Instant.now());

        String toString = videoEntity.toString();
        assertTrue(toString.contains("123"));
        assertTrue(toString.contains("Test Video"));
        assertTrue(toString.contains("/path/to/media.mp4"));
    }
}