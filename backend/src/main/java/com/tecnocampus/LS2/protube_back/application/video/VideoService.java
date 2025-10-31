package com.tecnocampus.LS2.protube_back.application.video;

import com.tecnocampus.LS2.protube_back.domain.video.*;
import com.tecnocampus.LS2.protube_back.exception.video.VideoNotFoundException;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.VideoMapper;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.application.dto.mapper.request.UpdateVideoRequest;
import com.tecnocampus.LS2.protube_back.web.dto.response.VideoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.*;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    public List<Video> listAll() {
        //TODO: crear metodo return all en el jparepository
        //return videoJpaRepository.listAll();
        return List.of();
    }

    public VideoResponse findById(String id) {
        return VideoMapper.toResponse(
                VideoEntityMapper.toDomain(
                        videoJpaRepository.findById(
                                new VideoId(id)).orElseThrow(()-> new VideoNotFoundException(id))));
        //return Optional.ofNullable(VideoMapper.toResponse(VideoEntityMapper.toDomain(videoJpaRepository.findById(new VideoId(id)).orElseThrow(() -> new VideoNotFoundException(id)))));
    }



    public Video createVideo(CreateVideoRequest request) {
        // Validaciones de negocio
        if (request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        // Construir dominio
        Video video = new Video(
                VideoId.generate(),
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
        videoJpaRepository.save(VideoEntityMapper.toEntity(video));
        return video;
    }

    public Optional<Video> updateVideo(String id, UpdateVideoRequest request) {
        var existing = videoJpaRepository.findById(new VideoId(id));
        if (existing.isEmpty()) return Optional.empty();

        // Lógica de actualización
        //TODO:Comprobar si esta bien (que se ha de cambiar?? solo llega el title y description, pero el mapper lo actualiza todo?)
        //var updated = existing.get().withTitle(request.title());
        var updated = existing.get();
        videoJpaRepository.save(updated);
        return Optional.of(VideoEntityMapper.toDomain(updated));
    }

    public void deleteVideo(String id) {
        //TODO: chekear si para decidir si borrarlo unicamente cuenta el id
        videoJpaRepository.delete(VideoEntityMapper.toEntity(new Video (new VideoId(id))));
    }

    public Optional<Video> getRandomVideo() {
        //TODO: chekear si esto esta bien (unicamente delega la llamada al jpaRepository)
        return Optional.of(VideoEntityMapper.toDomain(videoJpaRepository.getRandomVideo()));
    }
}
