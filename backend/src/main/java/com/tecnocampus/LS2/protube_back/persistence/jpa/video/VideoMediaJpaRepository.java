package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VideoMediaJpaRepository extends JpaRepository <VideoMediaEntity, UUID>{
    Optional<VideoMediaEntity> findByVideoId(UUID videoId);
}
