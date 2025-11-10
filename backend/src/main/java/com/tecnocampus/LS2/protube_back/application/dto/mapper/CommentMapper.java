package com.tecnocampus.LS2.protube_back.application.dto.mapper;

import com.tecnocampus.LS2.protube_back.domain.video.atributes.Comentario;
import com.tecnocampus.LS2.protube_back.persistence.jpa.video.atributes.ComentarioEntity;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    /**
     * Convert JPA Entity to Domain Model
     */
    public static Comentario toDomain(ComentarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Comentario(
                entity.getText(),
                entity.getUser(),
                entity.getTimestamp(),
                entity.getLikes()
        );
    }

    /**
     * Convert Domain Model to JPA Entity
     */
    public static ComentarioEntity toEntity(Comentario comentario) {
        if (comentario == null) {
            return null;
        }

        return new ComentarioEntity(
                comentario.getTexto(),
                comentario.getUsuario(),
                comentario.getTimestamp(),
                comentario.getLikes()
        );
    }

    /**
     * Convert a list of JPA Entities to a list of Domain Models
     */
    public static List<Comentario> toDomainList(List<ComentarioEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(entity -> new CommentMapper().toDomain(entity))
                .collect(Collectors.toList());
    }

    /**
     * Convert a list of Domain Models to a list of JPA Entities
     */
    public static List<ComentarioEntity> toEntityList(List<Comentario> comentarios) {
        if (comentarios == null || comentarios.isEmpty()) {
            return List.of();
        }

        return comentarios.stream()
                .map(CommentMapper::toEntity)
                .collect(Collectors.toList());
    }
}