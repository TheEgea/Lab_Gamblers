package com.tecnocampus.LS2.protube_back.api;

import com.tecnocampus.LS2.protube_back.application.dto.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.application.dto.response.VideoResponse;
import com.tecnocampus.LS2.protube_back.application.video.VideoService;
import com.tecnocampus.LS2.protube_back.application.video.VideoUploadService;
import com.tecnocampus.LS2.protube_back.domain.video.Video;


import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
@CrossOrigin(origins = "http://localhost:5173")
public class VideoController {

    private final VideoService videoService;
    private final VideoUploadService videoUploadService;

    public VideoController(VideoService videoService, VideoUploadService videoUploadService) {
        this.videoService = videoService;
        this.videoUploadService = videoUploadService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.listAll();
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/ids")
    public ResponseEntity<List<VideoId>> getAllVideoIds() {

        List<VideoId> videoIds = videoService.listAllVideoIds();
        return ResponseEntity.ok(videoIds);
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

    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(
            Authentication authentication,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tags", required = false) String tags) throws IOException {

        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String username = authentication.getName();

        Video video = videoUploadService.uploadVideo(
                file,
                title,
                description != null ? description : "",
                tags != null ? tags : "",
                username
        );

        Map<String, Object> response = new HashMap<>();
        response.put("id", video.getId().value().toString());
        response.put("title", video.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
