package com.tecnocampus.LS2.protube_back.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.domain.video.atributes.Comentario;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntity;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoEntityMapper;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.VideoJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class VideoDataLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(VideoDataLoader.class);

    private final VideoJpaRepository videoJpaRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${pro_tube.load_initial_data:false}")
    private boolean loadInitialData;

    @Value("${pro_tube.store.dir}")
    private String videosDir;

    public VideoDataLoader(VideoJpaRepository videoJpaRepository) {
        this.videoJpaRepository = videoJpaRepository;
    }

    @Override
    public void run(String... args) {
        if (!loadInitialData) {
            logger.info("Initial data loading disabled (pro_tube.load_initial_data=false)");
            return;
        }

        File dir = new File(videosDir);
        if (!dir.exists() || !dir.isDirectory()) {
            logger.warn("Videos directory not found or not a directory: {}", videosDir);
            return;
        }

        File[] jsonFiles = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            logger.warn("No JSON files found in {}", videosDir);
            return;
        }

        logger.info("Found {} json files in {}", jsonFiles.length, videosDir);

        int created = 0;
        int skipped = 0;

        for (File jsonFile : jsonFiles) {
            try {
                ProcessResult result = processJson(jsonFile);
                if (result == ProcessResult.CREATED)
                    created++;
                else
                    skipped++;
            } catch (Exception e) {
                logger.error("Error processing {}: {}", jsonFile.getName(), e.getMessage());
                skipped++;
            }
        }

        logger.info("Video loading completed: {} created, {} skipped from {} files", created, skipped, jsonFiles.length);
    }

    private enum ProcessResult {
        CREATED, SKIPPED
    }

    private ProcessResult processJson(File jsonFile) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(jsonFile);

        // Camps bàsics amb valors segurs per defecte
        String jsonId = jsonNode.path("id").asText("");
        int width = jsonNode.path("width").asInt(1920);
        int height = jsonNode.path("height").asInt(1080);
        Integer durationSeconds = (int) Math.round(jsonNode.path("duration").asDouble(0));
        String title = jsonNode.path("title").asText("");
        String user = jsonNode.path("user").asText("Unknown User");
        long tsUnix = jsonNode.path("timestamp").asLong(System.currentTimeMillis() / 1000);
        Instant createdAt = Instant.ofEpochSecond(tsUnix);

        // Validació bàsica
        if (jsonId.trim().isEmpty()) {
            logger.debug("Skipping {} - empty id", jsonFile.getName());
            return ProcessResult.SKIPPED;
        }
        if (title.trim().isEmpty()) {
            logger.debug("Skipping {} - empty title", jsonFile.getName());
            return ProcessResult.SKIPPED;
        }

        // Metadades
        JsonNode meta = jsonNode.path("meta");
        String description = meta.path("description").asText("");
        List<String> categories = extractStringList(meta.path("categories"));
        List<String> tags = extractStringList(meta.path("tags"));
        int viewCount = meta.path("view_count").asInt(0);
        int likeCount = meta.path("like_count").asInt(0);
        String channel = meta.path("channel").asText(user);

        // Comentaris
        List<Comentario> comments = extractComments(meta.path("comments"));

        // Fitxers media / thumbnail amb el mateix basename que el JSON
        String baseName = jsonFile.getName().substring(0, jsonFile.getName().lastIndexOf('.'));
        String mediaPath = baseName + ".mp4";
        String thumbnailPath = baseName + ".webp";

        // Verificar existència d'arxius físics
        File mediaFile = new File(jsonFile.getParentFile(), mediaPath);
        File thumbFile = new File(jsonFile.getParentFile(), thumbnailPath);
        if (!mediaFile.exists())
            logger.warn("Media file not found for {}: {}", jsonFile.getName(), mediaPath);
        if (!thumbFile.exists())
            logger.warn("Thumbnail file not found for {}: {}", jsonFile.getName(), thumbnailPath);

        Video video = new Video(
                VideoId.generate(),
                jsonId,
                width,
                height,
                durationSeconds,
                title,
                user,
                List.of(createdAt),
                description,
                categories,
                tags,
                viewCount,
                likeCount,
                channel,
                comments,
                mediaPath,
                thumbFile.exists() ? thumbnailPath : null,
                createdAt,
                Instant.now()
        );

        // Extensión para añadir multimedia directamente en la base de datos como bytes

        String basename = jsonFile.getName().substring(0, jsonFile.getName().lastIndexOf('.'));
        File videoFile = new File(jsonFile.getParentFile(), basename + ".mp4");
        File thumbnailFile = new File(jsonFile.getParentFile(), basename + ".webp");

        byte[] videoBytes = null;
        Long videoSize = null;
        byte[] thumbnailBytes = null;
        Long thumbnailSize = null;

        if (videoFile.exists()) {
            Path videoPath = videoFile.toPath();
            videoBytes = Files.readAllBytes(videoPath);
            videoSize = Files.size(videoPath);
            logger.debug("Loaded video file: {} ({} bytes)", videoFile.getName(), videoSize);
        } else {
            logger.warn("Video file not found: {}", videoFile.getName());
            return ProcessResult.SKIPPED;
        }

        if (thumbnailFile.exists()) {
            Path thumbPath = thumbnailFile.toPath();
            thumbnailBytes = Files.readAllBytes(thumbPath);
            thumbnailSize = Files.size(thumbPath);
            logger.debug("Loaded thumbnail file: {} ({} bytes)", thumbnailFile.getName(), thumbnailSize);
        } else {
            logger.warn("Thumbnail file not found: {}", thumbnailFile.getName());
            return ProcessResult.SKIPPED;
        }

        VideoEntity entity = VideoEntityMapper.toEntity(video);
        entity.setVideoBytes(videoBytes);
        entity.setVideoSize(videoSize);
        entity.setVideoMime("video/mp4");
        entity.setThumbnailBytes(thumbnailBytes);
        entity.setThumbnailSize(thumbnailSize);
        entity.setThumbnailMime("image/webp");

        videoJpaRepository.save(entity);
        logger.info("Inserted video {} with {} bytes + {} thumbnail bytes",
                jsonId, videoSize, thumbnailSize != null ? thumbnailSize : 0);

        // Hasta aquí la extensión

        //videoJpaRepository.save(VideoEntityMapper.toEntity(video));
        //logger.info("Inserted video {} - {}", jsonId, title);
        return ProcessResult.CREATED;
    }

    private List<String> extractStringList(JsonNode arrayNode) {
        List<String> list = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            arrayNode.forEach(node -> {
                String value = node.asText().trim();
                if (!value.isEmpty()) {
                    list.add(value);
                }
            });
        }
        return list;
    }

    private List<Comentario> extractComments(JsonNode commentsNode) {
        List<Comentario> comments = new ArrayList<>();
        if (commentsNode != null && commentsNode.isArray()) {
            commentsNode.forEach(commentNode -> {
                String text = commentNode.path("text").asText("").trim();
                String author = commentNode.path("author").asText("Anonymous");
                int likes = commentNode.path("like_count").asInt(0);
                long ts = commentNode.path("timestamp").asLong(System.currentTimeMillis() / 1000);
                Instant timestamp = Instant.ofEpochSecond(ts);

                // Solo añadir comentarios que tienen texto
                if (!text.isEmpty()) {
                    comments.add(new Comentario(text, author, timestamp, likes));
                }
            });
        }
        return comments;
    }
}
