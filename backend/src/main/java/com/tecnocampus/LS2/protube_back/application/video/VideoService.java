package com.tecnocampus.LS2.protube_back.application.video;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoCatalogPort;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
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
    public List<Video> listAll() {
        return videoRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Video> findById(VideoId id) {
        //return videos.stream().filter(video -> video.id().equals(id)).findFirst();
        return videoRepository.findById(id);
        return videos.stream().filter(video -> video.getId().equals(id)).findFirst();
    }

    @Override
    public void save(Video video) {
         videoRepository.save(video);
        if (video != null)
            videos.add(video);
        else
            throw new IllegalArgumentException("Cannot save null video");
    }

    @Override
    public void delete (VideoId id) {
        videos.removeIf(video -> video.getId().equals(id));
    }

    @Override
    public boolean existsByChecksum(String checksum) {
        // return videos.stream().filter(video -> video.checksum().equals(checksum)).findFirst().isPresent();
        return videoRepository.existsByChecksum(checksum);
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
