package com.gs.requalify.dto;

import com.gs.requalify.model.Checkpoint;
import com.gs.requalify.model.Course;
import com.gs.requalify.model.Roadmap;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO de resposta de roadmap")
public record RoadmapResponseDTO(
        @Schema(description = "ID do roadmap", example = "1")
        Long id,

        @Schema(description = "ID do usuário", example = "1")
        Long userId,

        @Schema(description = "Profissão objetivo", example = "Arquiteto de Soluções Cloud")
        String targetOccupation,

        @Schema(description = "Descrição do roadmap")
        String description,

        @Schema(description = "Lista de checkpoints do roadmap")
        List<CheckpointResponseDTO> checkpoints
) {
    public static RoadmapResponseDTO fromEntity(Roadmap roadmap) {
        return new RoadmapResponseDTO(
                roadmap.getId(),
                roadmap.getUser().getId(),
                roadmap.getTargetOccupation(),
                roadmap.getDescription(),
                roadmap.getCheckpoints() != null
                        ? roadmap.getCheckpoints().stream()
                        .map(CheckpointResponseDTO::fromEntity)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }

    @Schema(description = "DTO de resposta de checkpoint")
    public record CheckpointResponseDTO(
            @Schema(description = "ID do checkpoint", example = "1")
            Long id,

            @Schema(description = "Título do checkpoint", example = "Fundamentos de Cloud")
            String title,

            @Schema(description = "Descrição do checkpoint")
            String description,

            @Schema(description = "Ordem do checkpoint", example = "1")
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

    @Schema(description = "DTO de resposta de curso")
    public record CourseResponseDTO(
            @Schema(description = "ID do curso", example = "1")
            Long id,

            @Schema(description = "Nome do curso", example = "AWS Fundamentals")
            String name,

            @Schema(description = "Plataforma", example = "Udemy")
            String platform,

            @Schema(description = "URL do curso")
            String url,

            @Schema(description = "Descrição do curso")
            String description,

            @Schema(description = "Duração em horas", example = "40")
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
}