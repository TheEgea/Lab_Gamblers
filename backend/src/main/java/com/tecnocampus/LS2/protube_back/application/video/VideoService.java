package com.tecnocampus.LS2.protube_back.application.video;

import com.tecnocampus.LS2.protube_back.application.dto.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.application.dto.request.UpdateVideoRequest;
import com.tecnocampus.LS2.protube_back.domain.video.*;
import com.tecnocampus.LS2.protube_back.exception.video.VideoNotFoundException;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.VideoMapper;

import com.tecnocampus.LS2.protube_back.application.dto.response.VideoResponse;
import lombok.RequiredArgsConstructor;
import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.*;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoJpaRepository videoJpaRepository;
    private final VideoMapper videoMapper;
    private final VideoEntityMapper videoEntityMapper;


    public void save(CreateVideoRequest request) {

        Video video = videoMapper.toDomain(request);

        videoJpaRepository.save(VideoEntityMapper.toEntity(video));

    }

    // Métodos de casos de uso que USAN el puerto
    @Transactional
    public List<Video> listAll() {
        return videoJpaRepository.findAllMetadata().stream()
                .map(VideoMapper::VideoProjectionToDomain)  // ← Usar tu mapper existente
                .collect(Collectors.toList());
    }

    public List<VideoId> listAllVideoIds() {
        return videoJpaRepository.findAllIds();
    }

    public VideoResponse findById(String id) {
        VideoEntity entity = videoJpaRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException(id));
        Video video = VideoEntityMapper.toDomain(entity);
        return VideoMapper.toResponse(video);
    }



    public Video createVideo(CreateVideoRequest request) {
        // Validaciones de negocio
        if (request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        // Construir dominio
        Video video = new Video(
                request.id() != null ? new VideoId(request.id()) : VideoId.generate(),
                request.jsonId(),
                request.width(),
                request.height(),
                request.durationSeconds(),
                request.title(),
                request.user(),
                request.timestamp(),
                request.description(),
                request.categories(),
                request.tags(),
                0, // viewCount inicial
                0, // likeCount inicial
                request.channel(),
                List.of(), // comments vacío
                request.mediaPath(),
                request.thumbnailPath(),
                Instant.now(),
                Instant.now()
        );

        // Persistir usando el puerto
        VideoEntity entity = VideoEntityMapper.toEntity(video);
        videoJpaRepository.save(entity);
        return video;
    }

    @Transactional
    public Optional<Video> updateVideo(String id, UpdateVideoRequest request) {
        var existing = videoJpaRepository.findById(id);
        if (existing.isEmpty())
            return Optional.empty();

        // Lógica de actualización
        //TODO:Comprobar si esta bien (que se ha de cambiar?? solo llega el title y description, pero el mapper lo actualiza todo?)
        //var updated = existing.get().withTitle(request.title());
        VideoEntity entity = existing.get();
        if (request.title() != null)
            entity.setTitle(request.title());

        if (request.description() != null)
            entity.setDescription(request.description());
        entity.setUpdatedAt(Instant.now());

        VideoEntity updated = videoJpaRepository.save(entity);
        return Optional.of(VideoEntityMapper.toDomain(updated));
    }

    public void deleteVideo(String id) {
        //TODO: chekear si para decidir si borrarlo unicamente cuenta el id
        if (!videoJpaRepository.existsById(id))
            throw new VideoNotFoundException(id);
        videoJpaRepository.deleteById(id);
    }

    @Transactional
    public Optional<Video> getRandomVideo() {
        //TODO: chekear si esto esta bien (unicamente delega la llamada al jpaRepository)
        //return Optional.of(VideoEntityMapper.toDomain(videoJpaRepository.getRandomVideo()));
        try {
            VideoEntity entity = videoJpaRepository.getRandomVideo();
            return Optional.ofNullable(VideoEntityMapper.toDomain(entity));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
