package com.tecnocampus.LS2.protube_back.application.video;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoCatalogPort;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class VideoService implements VideoCatalogPort {

    // private final List<Video> videos = new ArrayList<>();
    private final VideoJpaRepository videoRepository;

    public VideoService(VideoJpaRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public List<VideoEntity> listAll() {
        return videoRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<VideoEntity> findById(VideoId id) {
        //return videos.stream().filter(video -> video.id().equals(id)).findFirst();
        return videoRepository.findById(id);
    }

    @Override
    public void save(VideoEntity video) {
         videoRepository.save(video);
    }

    @Override
    public void delete (VideoId id) {
        videos.removeIf(video -> video.getId().equals(id));
    }

    public boolean equals (List<Video> otherVideos) {
        if (otherVideos.size() != videos.size()) return false;
        for (int i = 0; i < videos.size(); i++) {
            if (!videos.get(i).equals(otherVideos.get(i))) return false;
        }
        return true;
    }

    @Override
    public Optional<Video> getRandomVideo(){
        return Optional.ofNullable(videos.get(new Random().nextInt(0, videos.size())));
    }

}
