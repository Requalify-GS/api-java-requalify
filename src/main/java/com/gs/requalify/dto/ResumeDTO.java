package com.gs.requalify.dto;

import com.gs.requalify.model.Certification;
import com.gs.requalify.model.Education;
import com.gs.requalify.model.Experience;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "DTO para criação/atualização de currículo")
public record ResumeDTO(

        @NotBlank(message = "A profissão não pode ser nula")
        @Size(min = 3, max = 150, message = "A profissão deve ter entre 3 e 150 caracteres")
        String occupation,

        @NotBlank(message = "O resumo profissional é obrigatório")
        @Size(min = 50, max = 1000, message = "O resumo deve ter entre 50 e 1000 caracteres")
        String summary,

        @Size(max = 20, message = "Máximo de 20 skills permitidas")
        List<@NotBlank(message = "Skill não pode estar vazia") String> skills,

        List<Education> educations,
        List<Experience> experiences,
        List<Certification> certifications
) {}
