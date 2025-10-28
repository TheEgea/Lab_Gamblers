package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoCatalogPort;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Primary // This will replace the in-memory VideoService implementation
@Transactional
public class JpaVideoCatalogAdapter implements VideoCatalogPort {

    private final VideoJpaRepository repository;
    private final VideoEntityMapper mapper;

    public JpaVideoCatalogAdapter(VideoJpaRepository repository, VideoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Video> listAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Video> listLatest(int page, int size) {
        var pageable = PageRequest.of(page, size);
        return repository.findAllByOrderByCreatedAtDesc(pageable)
                .map(mapper::toDomain)
                .getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(VideoId id) {
        return repository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public void save(Video video) {
        VideoEntity entity = mapper.toEntity(video);
        repository.save(entity);
    }

    @Override
    public void delete(VideoId id) {
        repository.deleteById(id.value());
    }

    @Override
    public Optional<Video> getRandomVideo() {
        //TODO
        return Optional.empty();
    }


    // Additional methods for extended functionality

    /**
     * Find video by checksum

    @Transactional(readOnly = true)
    public Optional<Video> findByChecksum(String checksum) {
        return repository.findByChecksum(checksum)
                .map(mapper::toDomain);
    }

    /**
     * Search videos by title or description
     */
    @Transactional(readOnly = true)
    public List<Video> searchByTitleOrDescription(String searchTerm) {
        return repository.findByTitleContainingIgnoreCase(searchTerm)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Video> searchByTitleOrDescription(String searchTerm, int page, int size) {
        var pageable = PageRequest.of(page, size);
        return repository.findByTitleOrDescriptionContaining(searchTerm, pageable)
                .map(mapper::toDomain)
                .getContent();
    }

    /**
     * Update existing video
     */
    public void update(Video video) {
        VideoEntity existingEntity = repository.findById(video.getId().value())
                .orElseThrow(() -> new IllegalArgumentException("Video not found: " + video.getId().value()));

        mapper.updateEntity(existingEntity, video);
        repository.save(existingEntity);
    }
}
