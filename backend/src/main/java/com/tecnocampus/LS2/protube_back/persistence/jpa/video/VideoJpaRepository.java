package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoJpaRepository extends JpaRepository<Video, VideoId> {
    List<Video> findAll();

    Optional<Video> findById(VideoId id);

    @Override
    Video save (Video video);

    boolean existsByChecksum(String checksum);
}
