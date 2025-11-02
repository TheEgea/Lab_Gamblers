package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.VideoMapper;
import com.tecnocampus.LS2.protube_back.application.dto.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.application.dto.request.UpdateVideoRequest;
import com.tecnocampus.LS2.protube_back.application.dto.response.VideoResponse;
import com.tecnocampus.LS2.protube_back.application.video.VideoService;
import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.exception.video.VideoNotFoundException;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntityMapper;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoJpaRepository videoJpaRepository;

    @Mock
    private VideoMapper videoMapper;

    @InjectMocks
    private VideoService videoService;


// Tests con datos validos
    @Test
    void testSaveVideo(){

        String id = UUID.randomUUID().toString();

        Video video = createSampleVideo(id);
        VideoEntity entity = createSampleVideoEntity(video);
        CreateVideoRequest request = createSampleCreateVideoRequest(id);


        when(videoMapper.toDomain(request)).thenReturn(video);

        videoService.save(request);

        verify(videoJpaRepository, times(1)).save(entity);
    }

    @Test
    void testListAllVideos() {
        String id = UUID.randomUUID().toString();
        Video video = createSampleVideo(id);
        VideoEntity entity = createSampleVideoEntity(video);

        when(videoJpaRepository.findAll()).thenReturn(List.of(entity));

        List<Video> videos = videoService.listAll();

        assertEquals(1, videos.size());
        assertEquals(video.getId(), videos.get(0).getId());
    }

    @Test
    void testFindById_VideoExists() {
        String id = UUID.randomUUID().toString();
        Video video = createSampleVideo(id);

        VideoResponse responseMock = createSampleVideoResponse(video);
        VideoEntity entity = createSampleVideoEntity(video);

        when(videoJpaRepository.findById(id)).thenReturn(Optional.of(entity));


        VideoResponse response = videoService.findById(id);

        assertEquals(responseMock, response);
    }

    @Test
    void testCreateVideo(){

        String id = UUID.randomUUID().toString();

        Video video = createSampleVideo(id);
        CreateVideoRequest request = createSampleCreateVideoRequest(id);

        when(videoJpaRepository.save(any())).thenReturn(null);
        Video createdVideo = videoService.createVideo(request);
        assertEquals(video.getId(), createdVideo.getId());
    }

    @Test
    void testUpdateVideo(){

        String id = UUID.randomUUID().toString();

        Video video = createSampleVideo(id);
        UpdateVideoRequest updateRequest = createSampleUpdateVideoRequest();
        VideoEntity entity = createSampleVideoEntity(video);

        when(videoJpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(videoJpaRepository.save(any())).thenReturn(entity);
        Video updatedVideo = videoService.updateVideo(id,updateRequest).get();
        assertEquals(video.getId(), updatedVideo.getId());
        assertEquals(updateRequest.title(), updatedVideo.getTitle());
        assertEquals(updateRequest.description(), updatedVideo.getDescription());
    }

    @Test
    void testDeleteVideo(){

        String id = UUID.randomUUID().toString();

        doNothing().when(videoJpaRepository).deleteById(id);
        when(videoJpaRepository.existsById(id)).thenReturn(true);

        videoService.deleteVideo(id);
        verify(videoJpaRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetRandomVideo(){

        String id = UUID.randomUUID().toString();

        Video video = createSampleVideo(id);

        when(videoJpaRepository.getRandomVideo()).thenReturn(Optional.of(createSampleVideoEntity(video)).get());

        Optional<Video> randomVideo = videoService.getRandomVideo();

        assertTrue(randomVideo.isPresent());
        assertEquals(video.getId(), randomVideo.get().getId());
    }

    //tests con datos invalidos

    @Test
    void testFindById_VideoDoesNotExist() {
        String id = UUID.randomUUID().toString();

        when(videoJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> {
            videoService.findById(id);
        });
    }

    @Test
    void testCreateVideo_NullDataRequest() {
        assertThrows(IllegalArgumentException.class, () -> {
            videoService.createVideo(new CreateVideoRequest(null, null, null, null, 0, 0, null, null, List.of(), null, List.of(), 0, 0, null, List.of(), null, null));
        });
    }

    @Test
    void testUpdateVideo_VideoDoesNotExist() {
        String id = UUID.randomUUID().toString();
        UpdateVideoRequest updateRequest = createSampleUpdateVideoRequest();

        when(videoJpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Video> updatedVideo = videoService.updateVideo(id, updateRequest);
        assertTrue(updatedVideo.isEmpty());
    }

    @Test
    void testUpdateVideo_NullData(){
        String id = UUID.randomUUID().toString();

        Video video = createSampleVideo(id);
        UpdateVideoRequest updateRequest = new UpdateVideoRequest(null, null);
        VideoEntity entity = createSampleVideoEntity(video);

        when(videoJpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(videoJpaRepository.save(any())).thenReturn(entity);
        Video updatedVideo = videoService.updateVideo(id,updateRequest).get();

        //no cambia el contenido valido
        assertEquals(video.getId(), updatedVideo.getId());
        assertEquals(video.getTitle(), updatedVideo.getTitle());
        assertEquals(video.getDescription(), updatedVideo.getDescription());
    }

    @Test
    void testDeleteVideo_VideoDoesNotExist(){

        String id = UUID.randomUUID().toString();

        when(videoJpaRepository.existsById(id)).thenReturn(false);

        assertThrows(VideoNotFoundException.class, () -> {
            videoService.deleteVideo(id);
        });
    }

    @Test
    void testGetRandomVideo_NoVideosAvailable(){

        when(videoJpaRepository.getRandomVideo()).thenReturn(null);

        Optional<Video> randomVideo = videoService.getRandomVideo();

        assertTrue(randomVideo.isEmpty());
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