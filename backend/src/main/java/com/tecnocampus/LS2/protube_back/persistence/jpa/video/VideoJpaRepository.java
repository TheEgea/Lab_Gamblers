package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import com.tecnocampus.LS2.protube_back.domain.video.Video;
import com.tecnocampus.LS2.protube_back.domain.video.VideoId;
import com.tecnocampus.LS2.protube_back.domain.video.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoJpaRepository extends JpaRepository<VideoEntity, String> {

    /**
     * Find videos by title containing text (case insensitive)
     */
    List<VideoEntity> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT v.id FROM VideoEntity v")
    List<VideoId> findAllIds();

    /**
     * Find all videos ordered by creation date (newest first)
     */
    Page<VideoEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find videos by title or description containing search term
     */
    @Query("SELECT v FROM VideoEntity v WHERE " +
            "LOWER(v.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<VideoEntity> findByTitleOrDescriptionContaining(
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    /**
     * Count videos by duration range
     */
    @Query("SELECT COUNT(v) FROM VideoEntity v WHERE " +
            "v.durationSeconds BETWEEN :minDuration AND :maxDuration")
    long countByDurationRange(
            @Param("minDuration") Integer minDuration,
            @Param("maxDuration") Integer maxDuration
    );

    /**
     * Find videos with no thumbnail
     */
    List<VideoEntity> findByThumbnailPathIsNull();

    @Query(value = "SELECT * FROM videos ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    VideoEntity getRandomVideo();

    @Query(value = "SELECT v FROM VideoEntity v")
    List<VideoEntity> findAll();

    // NUEVO - Query optimizada sin BLOBs
    @Query("""
        SELECT v.id as id,
               v.jsonId as jsonId,
               v.title as title,
               v.description as description,
               v.user as user,
               v.channel as channel,
               v.viewCount as viewCount,
               v.likeCount as likeCount,
               v.durationSeconds as durationSeconds,
               v.width as width,
               v.height as height,
               v.mediaPath as mediaPath,
               v.thumbnailPath as thumbnailPath,
               v.createdAt as createdAt,
               v.updatedAt as updatedAt
        FROM VideoEntity v
        ORDER BY v.createdAt DESC
        """)
    List<VideoProjection> findAllMetadata();
}
