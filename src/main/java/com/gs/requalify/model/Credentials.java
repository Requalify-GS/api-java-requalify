package com.gs.requalify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Credenciais de login")
public record Credentials(

        @NotBlank(message = "O e-mail não pode estar em branco")
        @Email(message = "O e-mail deve ser válido")
        @Schema(description = "Email do usuário", example = "alves@gmail.com")
        String username,

        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 5, message = "A senha deve ter pelo menos 5 caracteres")
        @Schema(description = "Senha do usuário", example = "12345")
        String password
) {}
