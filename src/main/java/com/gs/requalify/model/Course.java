package com.gs.requalify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de curso relacionado a um checkpoint")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do curso", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "checkpoint_id", nullable = false)
    @Schema(description = "Checkpoint associado")
    @JsonIgnore
    private Checkpoint checkpoint;

    @NotBlank(message = "O nome do curso não pode ser nulo")
    @Size(min = 3, max = 200, message = "O nome deve ter entre 3 e 200 caracteres")
    @Schema(description = "Nome do curso", example = "JavaScript Completo - Curso Gratuito")
    private String name;

    @NotBlank(message = "A plataforma não pode ser nula")
    @Size(min = 2, max = 100, message = "A plataforma deve ter entre 2 e 100 caracteres")
    @Schema(description = "Plataforma do curso", example = "YouTube")
    private String platform;

    @Pattern(regexp = "^(https?://).*", message = "URL deve começar com http:// ou https://")
    @Schema(description = "Link do curso", example = "https://www.youtube.com/watch?v=...")
    private String url;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    @Schema(description = "Descrição do curso")
    private String description;

    @Min(value = 1, message = "A duração deve ser no mínimo 1 hora")
    @Schema(description = "Duração estimada em horas", example = "20")
    private Integer durationHours;


}
