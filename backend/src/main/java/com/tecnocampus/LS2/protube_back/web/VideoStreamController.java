package com.tecnocampus.LS2.protube_back.web;

import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/media")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class VideoStreamController {
    private final VideoJpaRepository videoJpaRepository;

    public VideoStreamController(VideoJpaRepository videoJpaRepository) {
        this.videoJpaRepository = videoJpaRepository;
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<byte[]> streamVideo(
            @PathVariable String videoId,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {

        Optional<VideoEntity> opt = videoJpaRepository.findById(videoId);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        VideoEntity video = opt.get();
        byte[] videoBytes = video.getVideoBytes();

        if (videoBytes == null || videoBytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        long fileSize = video.getVideoSize() != null ? video.getVideoSize() : videoBytes.length;
        String mimeType = video.getVideoMime() != null ? video.getVideoMime() : "video/mp4";

        if (rangeHeader == null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .contentLength(fileSize)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .body(videoBytes);
        }

        try {
            String rangeValue = rangeHeader.replace("bytes=", "");
            long start = 0, end = fileSize - 1;

            if (rangeValue.contains("-")) {
                String[] parts = rangeValue.split("-", 2);
                if (!parts[0].isEmpty())
                    start = Long.parseLong(parts[0]);
                if (parts.length > 1 && !parts[1].isEmpty())
                    end = Long.parseLong(parts[1]);
            } else {
                start = Long.parseLong(rangeValue);
            }

            if (start < 0 || end >= fileSize || start > end) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                        .build();
            }

            int length = (int) (end - start + 1);
            byte[] slice = new byte[length];
            System.arraycopy(videoBytes, (int) start, slice, 0, length);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize)
                    .contentLength(length)
                    .body(slice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/thumbnail/{videoId}")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable String videoId) {
        Optional<VideoEntity> opt = videoJpaRepository.findById(videoId);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        VideoEntity video = opt.get();
        byte[] thumbnailBytes = video.getThumbnailBytes();

        if (thumbnailBytes == null || thumbnailBytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        String mimeType = video.getThumbnailMime() != null ? video.getThumbnailMime() : "image/webp";
        Long size = video.getThumbnailSize() != null ? video.getThumbnailSize() : (long) thumbnailBytes.length;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .contentLength(size)
                .body(thumbnailBytes);
        }
}
