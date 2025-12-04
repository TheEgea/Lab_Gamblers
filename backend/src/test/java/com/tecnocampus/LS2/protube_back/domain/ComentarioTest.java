package com.tecnocampus.LS2.protube_back.domain;

import com.tecnocampus.LS2.protube_back.application.dto.mapper.CommentMapper;
import com.tecnocampus.LS2.protube_back.domain.video.atributes.Comentario;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.atributes.ComentarioEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

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

    @Test
    void testMapperToDomain() {
        ComentarioEntity entity = new ComentarioEntity("Texto", "Usuario", Instant.now(), 5);

        Comentario comentario = CommentMapper.toDomain(entity);

        assertNotNull(comentario);
        assertEquals(entity.getText(), comentario.getTexto());
        assertEquals(entity.getUser(), comentario.getUsuario());
        assertEquals(entity.getTimestamp(), comentario.getTimestamp());
        assertEquals(entity.getLikes(), comentario.getLikes());
    }

    @Test
    void testMapperToEntity() {
        Comentario comentario = new Comentario("Texto", "Usuario", Instant.now(), 5);

        ComentarioEntity entity = CommentMapper.toEntity(comentario);

        assertNotNull(entity);
        assertEquals(comentario.getTexto(), entity.getText());
        assertEquals(comentario.getUsuario(), entity.getUser());
        assertEquals(comentario.getTimestamp(), entity.getTimestamp());
        assertEquals(comentario.getLikes(), entity.getLikes());
    }

    @Test
    void testMapperToDomainList() {
        ComentarioEntity entity1 = new ComentarioEntity("Texto1", "Usuario1", Instant.now(), 5);
        ComentarioEntity entity2 = new ComentarioEntity("Texto2", "Usuario2", Instant.now(), 10);

        List<Comentario> comentarios = CommentMapper.toDomainList(List.of(entity1, entity2));

        assertNotNull(comentarios);
        assertEquals(2, comentarios.size());
        assertEquals(entity1.getText(), comentarios.get(0).getTexto());
        assertEquals(entity2.getText(), comentarios.get(1).getTexto());
    }

    @Test
    void testMapperToEntityList() {
        Comentario comentario1 = new Comentario("Texto1", "Usuario1", Instant.now(), 5);
        Comentario comentario2 = new Comentario("Texto2", "Usuario2", Instant.now(), 10);

        List<ComentarioEntity> entities = CommentMapper.toEntityList(List.of(comentario1, comentario2));

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals(comentario1.getTexto(), entities.get(0).getText());
        assertEquals(comentario2.getTexto(), entities.get(1).getText());
    }

    @Test
    void testMapperNullAndEmptyLists() {
        assertTrue(CommentMapper.toDomainList(null).isEmpty());
        assertTrue(CommentMapper.toDomainList(List.of()).isEmpty());
        assertTrue(CommentMapper.toEntityList(null).isEmpty());
        assertTrue(CommentMapper.toEntityList(List.of()).isEmpty());
    }
}