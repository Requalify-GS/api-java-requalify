package com.gs.requalify.dto;

import com.gs.requalify.model.Checkpoint;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO de resposta de checkpoint")
public record CheckpointResponseDTO(
        @Schema(description = "ID do checkpoint", example = "1")
        Long id,

        @Schema(description = "Título", example = "Fundamentos de JavaScript")
        String title,

        @Schema(description = "Descrição")
        String description,

        @Schema(description = "Ordem", example = "1")
        Integer order,

        @Schema(description = "Lista de cursos")
        List<CourseResponseDTO> courses
) {
    public static CheckpointResponseDTO fromEntity(Checkpoint checkpoint) {
        return new CheckpointResponseDTO(
                checkpoint.getId(),
                checkpoint.getTitle(),
                checkpoint.getDescription(),
                checkpoint.getOrder(),
                checkpoint.getCourses() != null
                        ? checkpoint.getCourses().stream()
                        .map(CourseResponseDTO::fromEntity)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}