package com.gs.requalify.dto;

import com.gs.requalify.model.Course;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta de curso")
public record CourseResponseDTO(
        @Schema(description = "ID do curso", example = "1")
        Long id,

        @Schema(description = "Nome do curso")
        String name,

        @Schema(description = "Plataforma")
        String platform,

        @Schema(description = "URL do curso")
        String url,

        @Schema(description = "Descrição")
        String description,

        @Schema(description = "Duração em horas")
        Integer durationHours
) {
    public static CourseResponseDTO fromEntity(Course course) {
        return new CourseResponseDTO(
                course.getId(),
                course.getName(),
                course.getPlatform(),
                course.getUrl(),
                course.getDescription(),
                course.getDurationHours()
        );
    }
}