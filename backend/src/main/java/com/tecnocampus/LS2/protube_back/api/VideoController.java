package com.tecnocampus.LS2.protube_back.api;

import com.tecnocampus.LS2.protube_back.application.dto.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.application.dto.response.VideoResponse;
import com.tecnocampus.LS2.protube_back.application.video.VideoService;
import com.tecnocampus.LS2.protube_back.domain.video.Video;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.listAll();
        return ResponseEntity.ok(videos);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addVideo(@Valid @RequestBody CreateVideoRequest video) {
        videoService.save(video);
        return ResponseEntity.status(HttpStatus.CREATED).body("Video created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> findById(@Valid @PathVariable String id) {
        //public ResponseEntity<String> findById(@PathVariable String id) {
        //TODO:No se como devolverle un string por ahora he hecho que devuelva un VideoResponse
        return ResponseEntity.of(Optional.of(videoService.findById(id)));
    }

    @GetMapping("/random")
    public ResponseEntity<Video> getRandomVideo() {
        return ResponseEntity.of(videoService.getRandomVideo());
    }
}
