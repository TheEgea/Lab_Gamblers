package com.tecnocampus.LS2.protube_back.domain.video;

import java.util.List;
import java.util.Optional;

public interface VideoCatalogPort {
    List<Video> listAll();
    Optional<Video> findById(VideoId id);
    void save(Video video);
    void delete (VideoId id);
}