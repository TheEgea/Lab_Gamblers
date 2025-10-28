package com.tecnocampus.LS2.protube_back.application.video;

import com.tecnocampus.LS2.protube_back.domain.video.*;
import com.tecnocampus.LS2.protube_back.web.dto.mapper.VideoMapper;
import com.tecnocampus.LS2.protube_back.web.dto.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.web.dto.response.VideoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tecnocampus.LS2.protube_back.web.dto.*;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoJpaRepository videoJpaRepository;
    private final VideoMapper videoMapper;
    private final VideoEntityMapper videoEntityMapper;


    public void save(CreateVideoRequest request) {

        Video video = videoMapper.toDomain(request);

        videoJpaRepository.save(videoEntityMapper.toEntity(video));

    }

    // Métodos de casos de uso que USAN el puerto
    public List<Video> listAll() {
        return videoJpaRepository.listAll();
    }

    public VideoResponse findById(String id) throws Exception {
        return videoJpaRepository.findById(new VideoId(id));
    }



    public Video createVideo(CreateVideoRequest request) {
        // Validaciones de negocio
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        // Construir dominio
        Video video = new Video(
                VideoId.generate(),
                request.getJsonId(),
                request.getWidth(),
                request.getHeight(),
                request.getDurationSeconds(),
                request.getTitle(),
                request.getUser(),
                request.getTimestamp(),
                request.getDescription(),
                request.getCategories(),
                request.getTags(),
                0, // viewCount inicial
                0, // likeCount inicial
                request.getChannel(),
                List.of(), // comments vacío
                request.getMediaPath(),
                request.getThumbnailPath(),
                Instant.now(),
                Instant.now()
        );

        // Persistir usando el puerto
        videoJpaRepository.save(video);
        return video;
    }

    public Optional<Video> updateVideo(String id, UpdateVideoRequest request) {
        var existing = videoCatalogPort.findById(new VideoId(id));
        if (existing.isEmpty()) return Optional.empty();

        // Lógica de actualización
        var updated = existing.get().withTitle(request.getTitle());
        videoJpaRepository.save(updated);
        return Optional.of(updated);
    }

    public void deleteVideo(String id) {
        videoJpaRepository.delete(new VideoId(id));
    }

    public Optional<Video> getRandomVideo() {
        return videoJpaRepository.getRandomVideo();
    }
}
