package com.gs.requalify.dto;

import com.gs.requalify.model.Certification;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta de certificação")
public record CertificationResponseDTO(
        @Schema(description = "ID da certificação", example = "1")
        Long id,

        @Schema(description = "Nome da certificação", example = "AWS Certified Solutions Architect")
        String name,

        @Schema(description = "Organização emissora", example = "Amazon Web Services")
        String issuingOrganization
) {
    public static CertificationResponseDTO fromEntity(Certification certification) {
        return new CertificationResponseDTO(
                certification.getId(),
                certification.getName(),
                certification.getIssuingOrganization()
        );
    }
}