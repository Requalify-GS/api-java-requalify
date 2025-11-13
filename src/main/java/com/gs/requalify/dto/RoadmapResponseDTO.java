package com.gs.requalify.dto;

import com.gs.requalify.model.Roadmap;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO de resposta de roadmap")
public record RoadmapResponseDTO(
        @Schema(description = "ID do roadmap", example = "1")
        Long id,

        @Schema(description = "ID do usuário", example = "1")
        Long userId,

        @Schema(description = "Nome do usuário", example = "Guilherme Alves")
        String userName,

        @Schema(description = "Profissão objetivo", example = "Desenvolvedor Full Stack")
        String targetOccupation,

        @Schema(description = "Descrição do roadmap")
        String description,

        @Schema(description = "Lista de checkpoints")
        List<CheckpointResponseDTO> checkpoints
) {
    public static RoadmapResponseDTO fromEntity(Roadmap roadmap) {
        return new RoadmapResponseDTO(
                roadmap.getId(),
                roadmap.getUser().getId(),
                roadmap.getUser().getName(),
                roadmap.getTargetOccupation(),
                roadmap.getDescription(),
                roadmap.getCheckpoints() != null
                        ? roadmap.getCheckpoints().stream()
                        .map(CheckpointResponseDTO::fromEntity)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}
