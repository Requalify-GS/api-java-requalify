package com.gs.requalify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações de um usuário")
public record UserDTO(
        @Schema(description = "ID do usuário", example = "2")
        Long id,

        @Schema(description = "Email do usuário", example = "alves@gmail.com")
        String username,

        @Schema(description = "Nome completo do usuário", example = "Guilherme Alves")
        String name
) {}