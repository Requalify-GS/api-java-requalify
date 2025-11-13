package com.gs.requalify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "DTO para criação/atualização de roadmap")
public record RoadmapDTO(
        @NotBlank(message = "A profissão objetivo não pode ser nula")
        @Size(min = 3, max = 150)
        @Schema(description = "Profissão objetivo", example = "Desenvolvedor Full Stack")
        String targetOccupation,

        @Size(max = 500)
        @Schema(description = "Descrição do roadmap")
        String description,

        @Valid
        @Schema(description = "Lista de checkpoints")
        List<CheckpointDTO> checkpoints
) {}
