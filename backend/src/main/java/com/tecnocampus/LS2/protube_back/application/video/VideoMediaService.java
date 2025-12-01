package com.tecnocampus.LS2.protube_back.application.video;

import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoMediaEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoMediaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoMediaService {

    private final VideoMediaJpaRepository repository;

    public void saveMediaForVideo(UUID videoId,
                                  byte[] mediaBytes,
                                  byte[] thumbnailBytes,
                                  Integer width,
                                  Integer height,
                                  Integer durationSeconds) {

        VideoMediaEntity entity = repository.findByVideoId(videoId)
                .orElseGet(VideoMediaEntity::new);

        entity.setVideoId(videoId);
        entity.setMediaBytes(mediaBytes);
        entity.setThumbnailBytes(thumbnailBytes);
        entity.setWidth(width);
        entity.setHeight(height);
        entity.setDurationSeconds(durationSeconds);

        repository.save(entity);
    }

    public VideoMediaEntity getByVideoIdOrThrow(UUID videoId) {
        return repository.findByVideoId(videoId)
                .orElseThrow(() -> new IllegalStateException("Media not found for video " + videoId));
    }
}
