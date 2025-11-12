package com.gs.requalify.dto;


import com.gs.requalify.model.Resume;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO de resposta de currículo")
public record ResumeResponseDTO(
        @Schema(description = "ID do currículo", example = "1")
        Long id,

        @Schema(description = "ID do usuário", example = "1")
        Long userId,

        @Schema(description = "Nome do usuário", example = "Guilherme Alves")
        String userName,

        @Schema(description = "Email do usuário", example = "alves@gmail.com")
        String userEmail,

        @Schema(description = "Profissão", example = "Desenvolvedor Java")
        String occupation,

        @Schema(description = "Resumo profissional")
        String summary,

        @Schema(description = "Lista de habilidades")
        List<String> skills,

        @Schema(description = "Lista de formações educacionais")
        List<EducationResponseDTO> educations,

        @Schema(description = "Lista de experiências profissionais")
        List<ExperienceResponseDTO> experiences,

        @Schema(description = "Lista de certificações")
        List<CertificationResponseDTO> certifications
) {
    public static ResumeResponseDTO fromEntity(Resume resume) {
        return new ResumeResponseDTO(
                resume.getId(),
                resume.getUser().getId(),
                resume.getUser().getName(),
                resume.getUser().getUsername(),
                resume.getOccupation(),
                resume.getSummary(),
                resume.getSkills(),
                resume.getEducations() != null
                        ? resume.getEducations().stream()
                        .map(EducationResponseDTO::fromEntity)
                        .collect(Collectors.toList())
                        : List.of(),
                resume.getExperiences() != null
                        ? resume.getExperiences().stream()
                        .map(ExperienceResponseDTO::fromEntity)
                        .collect(Collectors.toList())
                        : List.of(),
                resume.getCertifications() != null
                        ? resume.getCertifications().stream()
                        .map(CertificationResponseDTO::fromEntity)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}