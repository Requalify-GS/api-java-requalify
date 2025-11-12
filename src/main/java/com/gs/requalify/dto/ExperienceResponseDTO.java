package com.gs.requalify.dto;

import com.gs.requalify.model.Experience;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO de resposta de experiência profissional")
public record ExperienceResponseDTO(
        @Schema(description = "ID da experiência", example = "1")
        Long id,

        @Schema(description = "Nome da empresa", example = "Tech Solutions LTDA")
        String company,

        @Schema(description = "Cargo", example = "Desenvolvedor Backend Sênior")
        String position,

        @Schema(description = "Data de início", example = "2021-06-01")
        LocalDate startDate,

        @Schema(description = "Data de saída", example = "2024-08-31")
        LocalDate endDate,

        @Schema(description = "Indica se ainda está trabalhando", example = "true")
        Boolean currentJob,

        @Schema(description = "Descrição das atividades")
        String description
) {
    public static ExperienceResponseDTO fromEntity(Experience experience) {
        return new ExperienceResponseDTO(
                experience.getId(),
                experience.getCompany(),
                experience.getPosition(),
                experience.getStartDate(),
                experience.getEndDate(),
                experience.getCurrentJob(),
                experience.getDescription()
        );
    }
}