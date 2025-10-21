package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoCatalogPort;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService implements VideoCatalogPort {

    private final List<Video> videos = new ArrayList<>();

    @Override
    public List<Video> listAll() {
        return videos;
    }

    @Override
    public Optional<Video> findById(VideoId id) {
        return videos.stream().filter(video -> video.id().equals(id)).findFirst();
    }

    @Override
    public void save(Video video) {
         videos.add(video);
    }

    @Override
    public void delete (VideoId id) {
        videos.removeIf(video -> video.id().equals(id));
    }

    @Override
    public boolean existsByChecksum(String checksum) {
        return videos.stream().filter(video -> video.checksum().equals(checksum)).findFirst().isPresent();
    }

    public boolean equals (List<Video> otherVideos) {
        if (otherVideos.size() != videos.size()) return false;
        for (int i = 0; i < videos.size(); i++) {
            if (!videos.get(i).equals(otherVideos.get(i))) return false;
        }
        return true;
    }
}
