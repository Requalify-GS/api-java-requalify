package com.gs.requalify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criar um novo roadmap")
public record RoadmapDTO(
        @NotBlank(message = "A profissão objetivo não pode ser nula")
        @Size(min = 3, max = 150, message = "Deve ter entre 3 e 150 caracteres")
        @Schema(description = "Profissão objetivo", example = "Arquiteto de Soluções Cloud")
        String targetOccupation,

        @NotBlank(message = "A descrição não pode ser nula")
        @Size(min = 10, max = 500, message = "Deve ter entre 10 e 500 caracteres")
        @Schema(description = "Descrição do objetivo de carreira",
                example = "Transição de desenvolvedor Java para Arquiteto de Soluções Cloud com foco em AWS")
        String description
) {}