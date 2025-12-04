package com.tecnocampus.LS2.protube_back.persistance.jpa.video.atributes;

import com.tecnocampus.LS2.protube_back.persistence.jpa.video.atributes.ComentarioEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioEntityTest {

    @Test
    void testConstructorValues() {

        String text = "This is a comment";
        String user = "testUser";
        Instant timestamp = Instant.now();
        int likes = 10;

        ComentarioEntity comment = new ComentarioEntity(text, user, timestamp, likes);

        assertEquals(text, comment.getText());
        assertEquals(user, comment.getUser());
        assertEquals(timestamp, comment.getTimestamp());
        assertEquals(likes, comment.getLikes());
    }

    @Test
    void testSettersAndGetters_Text() {
        ComentarioEntity comment = new ComentarioEntity();
        String text = "Updated comment";

        comment.setText(text);

        assertEquals(text, comment.getText());
    }

    @Test
    void testSettersAndGetters_User() {
        ComentarioEntity comment = new ComentarioEntity();
        String user = "updatedUser";

        comment.setUser(user);

        assertEquals(user, comment.getUser());
    }

    @Test
    void testSettersAndGetters_Timestamp() {
        ComentarioEntity comment = new ComentarioEntity();
        Instant timestamp = Instant.now();

        comment.setTimestamp(timestamp);

        assertEquals(timestamp, comment.getTimestamp());
    }

    @Test
    void testSettersAndGetters_Likes() {
        ComentarioEntity comment = new ComentarioEntity();
        int likes = 20;

        comment.setLikes(likes);

        assertEquals(likes, comment.getLikes());
    }
}