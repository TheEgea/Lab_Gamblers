package com.tecnocampus.LS2.protube_back.application.video;

import com.tecnocampus.LS2.protube_back.application.dto.request.CreateVideoRequest;
import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.domain.video.atributes.Comentario;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoUploadService {

    @Value("${pro_tube.store.dir")
    private String storeDir;

    private final VideoService videoService;
    private final VideoJpaRepository videoJpaRepository;

    private record VideoMeta(int width, int height, Integer durationSeconds) {}

    private VideoMeta extractMetadata(Path videoPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-select_streams", "v:0",
                    "-show_entries", "stream=width,height,duration",
                    "-of", "csv=p=0:s=x",
                    videoPath.toAbsolutePath().toString()
            );
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split("x");
                    int width = Integer.parseInt(parts[0]);
                    int height = Integer.parseInt(parts[1]);
                    int durationSeconds = (int) Math.round(Double.parseDouble(parts[2]));
                    return new VideoMeta(width, height, durationSeconds);
                }
            }
        } catch (Exception e) {
            System.err.println("Error extrayendo metadatos de vídeo: " + e.getMessage());
        }
        return new VideoMeta(0, 0, null);
    }

    public Video uploadVideo(MultipartFile file,
                             String title,
                             String description,
                             String tagsRaw,
                             String username) throws IOException {

        Path baseDir = Paths.get(storeDir);
        if (!Files.exists(baseDir)) {
            Files.createDirectories(baseDir);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        if (extension == null || extension.isBlank()) {
            extension = "mp4";
        }

        String fileBaseName = UUID.randomUUID().toString();
        String videoFileName = fileBaseName + "." + extension;
        Path videoPath = baseDir.resolve(videoFileName);
        Files.copy(file.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);

        VideoMeta meta = extractMetadata(videoPath);
        int width = meta.width();
        int height = meta.height();
        Integer durationSeconds = meta.durationSeconds();

        String id = null;
        String jsonId = null;
        List<Instant> timestamp = new ArrayList<>();
        List<String> categories = new ArrayList<>();

        List<String> tags = new ArrayList<>();
        if (tagsRaw != null && !tagsRaw.isBlank()) {
            for (String t : tagsRaw.split(",")) {
                String trimmed = t.trim();
                if (!trimmed.isEmpty()) {
                    tags.add(trimmed);
                }
            }
        }

        int viewCount = 0;
        int likeCount = 0;
        String channel = username;
        List<Comentario> comments = new ArrayList<>();
        String mediaPath = videoFileName;
        String thumbnailPath = null;

        CreateVideoRequest request = new CreateVideoRequest(
                title,
                description,
                id,
                jsonId,
                width,
                height,
                durationSeconds,
                username,
                timestamp,
                categories,
                tags,
                viewCount,
                likeCount,
                channel,
                comments,
                mediaPath,
                thumbnailPath
        );

        Video video = videoService.createVideo(request);

        byte[] mediaBytes = Files.readAllBytes(videoPath);

        String videoIdStr = video.getId().value().toString();
        VideoEntity entity = videoJpaRepository.findById(videoIdStr)
                        .orElseThrow(() -> new IllegalStateException("Video " + videoIdStr + " not found"));
        entity.setVideoBytes(mediaBytes);
        entity.setVideoSize((long) mediaBytes.length);
        String mime = file.getContentType() != null ? file.getContentType() : "video/mp4";
        entity.setVideoMime(mime);

        String thumbFileName = fileBaseName + ".webp";
        Path thumbPath = baseDir.resolve(thumbFileName);

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-ss", "00:00:01",
                    "-i", videoPath.toAbsolutePath().toString(),
                    "-frames:v", "1",
                    "-vf", "scale=320:-1",
                    thumbPath.toAbsolutePath().toString()
            );
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();

            if (Files.exists(thumbPath)) {
                byte[] thumbBytes = Files.readAllBytes(thumbPath);
                entity.setThumbnailBytes(thumbBytes);
                entity.setThumbnailSize((long) thumbBytes.length);
                entity.setThumbnailMime("image/webp"); // si usas .jpg: "image/jpeg"
            }
        } catch (Exception ex) {
            System.err.println("No se pudo generar la miniatura: " + ex.getMessage());
        }

        videoJpaRepository.save(entity);

        return video;
    }

    private String getExtension(String filename) {
        if (filename == null) return null;
        int idx = filename.lastIndexOf('.');
        if (idx == -1) return null;
        return filename.substring(idx + 1).toLowerCase();
    }
}
