package com.tecnocampus.LS2.protube_back.persistence.jpa.video.atributes;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "comments")
public class ComentarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "user", nullable = false)
    private String user;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "likes", nullable = false)
    private int likes;

    // Default constructor (required by JPA)
    public ComentarioEntity() {}

    public ComentarioEntity(String text, String user, Instant timestamp, int likes) {
        this.text = text;
        this.user = user;
        this.timestamp = timestamp;
        this.likes = likes;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
}