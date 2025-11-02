package com.tecnocampus.LS2.protube_back.domain.video.atributes;

import lombok.Getter;

import java.time.Instant;

@Getter
public class Comentario {

    private final String texto;
    private final String usuario;
    private final Instant timestamp;
    private final int likes;

    public Comentario(String texto, String usuario, Instant timestamp, int likes) {
        this.texto = texto;
        this.usuario = usuario;
        this.timestamp = timestamp;
        this.likes = likes;
    }

}
