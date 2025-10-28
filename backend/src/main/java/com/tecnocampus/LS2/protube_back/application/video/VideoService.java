package com.tecnocampus.LS2.protube_back.application.video;

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
        return videos.stream().filter(video -> video.getId().equals(id)).findFirst();
    }

    @Override
    public void save(Video video) {
         videos.add(video);
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
    public VideoService getRandomVideo(){
        return this;
    }

}
