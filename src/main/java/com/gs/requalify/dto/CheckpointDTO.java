package com.gs.requalify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "DTO para checkpoint")
public record CheckpointDTO(
        @NotBlank(message = "O título não pode ser nulo")
        @Size(min = 3, max = 200)
        @Schema(description = "Título do checkpoint", example = "Fundamentos de JavaScript")
        String title,

        @NotBlank(message = "A descrição não pode ser nula")
        @Size(min = 10, max = 1000)
        @Schema(description = "Descrição do checkpoint")
        String description,

        @NotNull(message = "A ordem não pode ser nula")
        @Positive
        @Schema(description = "Ordem do checkpoint", example = "1")
        Integer order,

        @Valid
        @Schema(description = "Lista de cursos")
        List<CourseDTO> courses
) {}