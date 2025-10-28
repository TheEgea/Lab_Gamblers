package com.tecnocampus.LS2.protube_back.controller;
/*
import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoCatalogPort;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class VideosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private VideoCatalogPort videoCatalogPort;

    @InjectMocks
    private VideoController videoController;

    @Test
    void getAllVideos_ShouldReturnListOfVideos() {
        List<Video> videos = List.of(createSampleVideo("1"));
        when(videoCatalogPort.listAll()).thenReturn(videos);

        Object result = videoController.getAllVideos();

        assertEquals(videos, result);

    }

    @Test
    void addVideo_ShouldReturnSuccessMessage() {
        ResponseEntity<String> response = videoController.addVideo(createSampleVideo("3"));

        assertEquals("Video added successfully", response.getBody());
    }

    @Test
    void findById_ShouldReturnVideoIfExists() {
        Video video = createSampleVideo("1");
        when(videoCatalogPort.findById(new VideoId("1"))).thenReturn(Optional.of(video));

        ResponseEntity<Video> response = videoController.findById(new VideoId("1"));

        assertEquals(video, response.getBody());
        verify(videoCatalogPort, times(1)).findById(new VideoId("1"));
    }

    @Test
    void findById_ShouldReturnNotFoundIfVideoDoesNotExist() {
        when(videoCatalogPort.findById(new VideoId("1"))).thenReturn(Optional.empty());

        ResponseEntity<Video> response = videoController.findById(new VideoId("1"));

        assertEquals(404, response.getStatusCodeValue());
        verify(videoCatalogPort, times(1)).findById(new VideoId("1"));
    }

    @Test
    void addVideo_ShouldThrowExceptionForInvalidInputv2(){

        //mockMvc.perform(/)


        //long nonExistingCustomerId = Long.MAX_VALUE;
        //mockMvc.perform(get(BASE_URL + "/{id}", nonExistingCustomerId))
        //       .andExpect(status().isNotFound());
    }

    @Test
    void addVideo_ShouldThrowExceptionForInvalidInput() {
        // Simula un caso donde el video tiene datos inválidos
        doThrow(new IllegalArgumentException("Invalid video data"))
                .when(videoCatalogPort).save(any(Video.class));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            videoController.addVideo(null);
        });

        assertEquals("Invalid video data", exception.getMessage());
    }


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
                List.of(),
                "media/path",
                "thumbnail/path",
                Instant.now(),
                Instant.now()
        );
    }
}