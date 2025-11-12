package com.gs.requalify.model;

import io.swagger.v3.oas.annotations.media.Schema;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Schema(description = "Token de autenticação retornado após login")
public record Token(
        @Schema(description = "JWT gerado para autenticação")
        String token,

        @Schema(description = "Email do usuário autenticado", example = "alves@gmail.com")
        String username,

        @Schema(description = "Nome completo do usuário autenticado", example = "Guilherme Alves")
        Long id
) {}

