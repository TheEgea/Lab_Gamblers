// backend/src/main/java/com/tecnocampus/LS2/protube_back/persistence/jpa/video/VideoProjection.java
package com.tecnocampus.LS2.protube_back.persistence.jpa.video;

import java.time.Instant;
import java.util.List;

/**
 * Projection para cargar solo metadata de videos, sin BLOBs
 */
public interface VideoProjection {
    String getId();
    String getJsonId();
    String getTitle();
    String getDescription();
    String getUser();
    String getChannel();
    int getViewCount();
    int getLikeCount();
    Integer getDurationSeconds();
    int getWidth();
    int getHeight();
    String getMediaPath();
    String getThumbnailPath();
    Instant getCreatedAt();
    Instant getUpdatedAt();

    // NO incluir getVideoBytes() ni getThumbnailBytes()
}
