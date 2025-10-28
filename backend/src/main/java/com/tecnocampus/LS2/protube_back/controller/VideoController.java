package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.application.video.VideoService;
import com.tecnocampus.LS2.protube_back.domain.video.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.listAll();
        return ResponseEntity.ok(videos);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addVideo(@RequestBody CreateVideoRequest video) {
        videoService.save(video);
        return ResponseEntity.status(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable String id) {
        return ResponseEntity.of(videoService.findById(id));
    }

    @GetMapping("/random")
    public ResponseEntity<Video> getRandomVideo() {
        return ResponseEntity.of(videoService.getRandomVideo());
    }
}
