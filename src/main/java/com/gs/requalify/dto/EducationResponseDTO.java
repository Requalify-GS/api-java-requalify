package com.gs.requalify.dto;

import com.gs.requalify.model.Education;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO de resposta de formação educacional")
public record EducationResponseDTO(
        @Schema(description = "ID da formação", example = "1")
        Long id,

        @Schema(description = "Nome da instituição", example = "FIAP")
        String institution,

        @Schema(description = "Nome do curso", example = "Análise e Desenvolvimento de Sistemas")
        String course,

        @Schema(description = "Nível de formação", example = "Graduação")
        String educationLevel,

        @Schema(description = "Data de início", example = "2021-02-01")
        LocalDate startDate,

        @Schema(description = "Data de conclusão", example = "2023-12-15")
        LocalDate endDate,

        @Schema(description = "Indica se está em andamento", example = "false")
        Boolean inProgress
) {
    public static EducationResponseDTO fromEntity(Education education) {
        return new EducationResponseDTO(
                education.getId(),
                education.getInstitution(),
                education.getCourse(),
                education.getEducationLevel(),
                education.getStartDate(),
                education.getEndDate(),
                education.getInProgress()
        );
    }
}