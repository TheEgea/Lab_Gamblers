package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoCatalogPort;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideosController {

    private final VideoCatalogPort videoCatalogPort;

    public VideosController(VideoCatalogPort videoCatalogPort) {
        this.videoCatalogPort = videoCatalogPort;
    }

    // TODO: cambiar a respuesta valida
    @GetMapping("/getAll")
    public ResponseEntity<List<String>> getVideos() {
        return ResponseEntity.ok().body(videoCatalogPort.listAll().stream().map(v -> v.title()).toList());
    }

    @PostMapping("/addVideos")
    public ResponseEntity<String> addVideo(Video video) {
        videoCatalogPort.save(video);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findById")
    public ResponseEntity<Video> findById(VideoId videoId) {
        return ResponseEntity.of(videoCatalogPort.findById(videoId));
    }
}
