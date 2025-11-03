package com.tecnocampus.LS2.protube_back.domain;

import com.tecnocampus.LS2.protube_back.domain.video.atributes.Comentario;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioTest {

    @Test
    void validComentario() {
        Instant now = Instant.now();
        Comentario comentario = new Comentario("Texto del comentario", "Usuario1", now, 10);

        assertEquals("Texto del comentario", comentario.getTexto());
        assertEquals("Usuario1", comentario.getUsuario());
        assertEquals(now, comentario.getTimestamp());
        assertEquals(10, comentario.getLikes());
    }


    @Test
    void comentarioWithNegativeLikes() {
        Instant now = Instant.now();
        Comentario comentario = new Comentario("Texto del comentario", "Usuario1", now, -5);

        assertEquals(-5, comentario.getLikes());
    }
}