package com.gs.requalify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para atualização de usuário")
public record UserUpdateDTO(
        @Size(min = 1, max = 255, message = "O nome deve ter entre 1 e 255 caracteres")
        @Schema(description = "Nome completo do usuário", example = "Guilherme Alves Silva")
        String name,

        @Size(min = 5, message = "A senha deve ter pelo menos 5 caracteres")
        @Schema(description = "Nova senha do usuário", example = "novaSenha123")
        String password,

        @NotBlank(message = "O e-mail do usuário não pode ser nulo")
        @Email(message = "O e-mail do usuário deve ser válido")
        @Schema(description = "Email do usuário", example = "alves@gmail.com")
        String username
) {}
