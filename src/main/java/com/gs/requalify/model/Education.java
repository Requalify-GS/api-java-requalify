package com.gs.requalify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de formação educacional")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da formação educacional", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    @Schema(description = "Currículo associado")
    @JsonIgnore
    private Resume resume;

    @NotBlank(message = "A instituição de ensino não pode ser nula")
    @Size(min = 1, max = 200)
    @Schema(description = "Nome da instituição de ensino", example = "Universidade de São Paulo")
    private String institution;

    @NotBlank(message = "O curso não pode ser nulo")
    @Size(min = 1, max = 200)
    @Schema(description = "Nome do curso", example = "Ciência da Computação")
    private String course;

    @NotBlank(message = "O nível de formação não pode ser nulo")
    @Size(min = 1, max = 50)
    @Schema(description = "Nível de formação", example = "Graduação")
    private String educationLevel;

    @NotNull(message = "A data de início não pode ser nula")
    @Schema(description = "Data de início do curso", example = "2020-03-01")
    private LocalDate startDate;

    @Schema(description = "Data de conclusão do curso", example = "2024-12-15")
    private LocalDate endDate;

    @Schema(description = "Indica se o curso está em andamento", example = "false")
    private Boolean inProgress;
}