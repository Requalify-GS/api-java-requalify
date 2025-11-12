package com.gs.requalify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de certificação")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da certificação", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    @Schema(description = "Currículo associado")
    @JsonIgnore
    private Resume resume;

    @NotBlank(message = "O nome da certificação não pode ser nulo")
    @Size(min = 1, max = 200)
    @Schema(description = "Nome da certificação", example = "AWS Certified Solutions Architect")
    private String name;

    @NotBlank(message = "A instituição emissora não pode ser nula")
    @Size(min = 1, max = 200)
    @Schema(description = "Instituição que emitiu a certificação", example = "Amazon Web Services")
    private String issuingOrganization;

}