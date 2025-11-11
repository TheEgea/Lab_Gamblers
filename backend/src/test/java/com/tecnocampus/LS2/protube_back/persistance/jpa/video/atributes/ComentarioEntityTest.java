package com.tecnocampus.LS2.protube_back.persistance.jpa.video.atributes;

import com.tecnocampus.LS2.protube_back.persistence.jpa.video.atributes.ComentarioEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioEntityTest {

    @Test
    void testConstructorValues() {
        // Arrange
        String text = "This is a comment";
        String user = "testUser";
        Instant timestamp = Instant.now();
        int likes = 10;

        // Act
        ComentarioEntity comment = new ComentarioEntity(text, user, timestamp, likes);

        // Assert
        assertEquals(text, comment.getText());
        assertEquals(user, comment.getUser());
        assertEquals(timestamp, comment.getTimestamp());
        assertEquals(likes, comment.getLikes());
    }

    @Test
    void testSettersAndGetters_Text() {
        // Arrange
        ComentarioEntity comment = new ComentarioEntity();
        String text = "Updated comment";

        // Act
        comment.setText(text);

        // Assert
        assertEquals(text, comment.getText());
    }

    @Test
    void testSettersAndGetters_User() {
        // Arrange
        ComentarioEntity comment = new ComentarioEntity();
        String user = "updatedUser";

        // Act
        comment.setUser(user);

        // Assert
        assertEquals(user, comment.getUser());
    }

    @Test
    void testSettersAndGetters_Timestamp() {
        // Arrange
        ComentarioEntity comment = new ComentarioEntity();
        Instant timestamp = Instant.now();

        // Act
        comment.setTimestamp(timestamp);

        // Assert
        assertEquals(timestamp, comment.getTimestamp());
    }

    @Test
    void testSettersAndGetters_Likes() {
        // Arrange
        ComentarioEntity comment = new ComentarioEntity();
        int likes = 20;

        // Act
        comment.setLikes(likes);

        // Assert
        assertEquals(likes, comment.getLikes());
    }
}