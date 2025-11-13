package com.gs.requalify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para curso")
public record CourseDTO(
        @NotBlank(message = "O nome do curso não pode ser nulo")
        @Size(min = 3, max = 200)
        @Schema(description = "Nome do curso", example = "JavaScript Completo")
        String name,

        @NotBlank(message = "A plataforma não pode ser nula")
        @Size(min = 2, max = 100)
        @Schema(description = "Plataforma", example = "YouTube")
        String platform,

        @Pattern(regexp = "^(https?://).*", message = "URL inválida")
        @Schema(description = "Link do curso")
        String url,

        @Size(max = 500)
        @Schema(description = "Descrição do curso")
        String description,

        @Schema(description = "Duração em horas", example = "20")
        Integer durationHours
) {}
