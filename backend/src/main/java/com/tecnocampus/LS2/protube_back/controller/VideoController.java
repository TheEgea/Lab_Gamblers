package com.tecnocampus.LS2.protube_back.controller;


import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoCatalogPort;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoCatalogPort videoCatalogPort;

    public VideoController(VideoCatalogPort videoCatalogPort) {
        this.videoCatalogPort = videoCatalogPort;
    }

    @GetMapping("/getAll")
    public Object getAllVideos() {
        return videoCatalogPort.listAll();
    }

    @PostMapping("/addVideo")
    public ResponseEntity<String> addVideo() {
        // Implementation for adding a video would go here
        return ResponseEntity.ok("Video added successfully");
    }

    @GetMapping("/findById")
    public ResponseEntity<Video> findById(VideoId videoId) {
        return ResponseEntity.of(videoCatalogPort.findById(videoId));
    }

    @GetMapping("/gambling")
    public VideoController getGamblingVideos() {
        // Implementatio for getting a random video
        return ResponseEntity.of(videoCatalogPort.getRandomVideo());

    }

}
