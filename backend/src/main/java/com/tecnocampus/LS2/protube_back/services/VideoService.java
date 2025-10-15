package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoCatalogPort;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.domain.video.VideoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService implements VideoCatalogPort {

    // private final List<Video> videos = new ArrayList<>();
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
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
    }

    @Override
    public void save(Video video) {
         videoRepository.save(video);
    }

    @Override
    public boolean existsByChecksum(String checksum) {
        // return videos.stream().filter(video -> video.checksum().equals(checksum)).findFirst().isPresent();
        return videoRepository.existsByChecksum(checksum);
    }
}
