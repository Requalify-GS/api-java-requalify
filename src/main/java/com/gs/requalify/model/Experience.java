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
@Schema(description = "Modelo de experiência profissional")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da experiência profissional", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    @Schema(description = "Currículo associado")
    @JsonIgnore
    private Resume resume;

    @NotBlank(message = "A empresa não pode ser nula")
    @Size(min = 1, max = 200)
    @Schema(description = "Nome da empresa", example = "Tech Solutions LTDA")
    private String company;

    @NotBlank(message = "O cargo não pode ser nulo")
    @Size(min = 1, max = 150)
    @Schema(description = "Cargo ocupado", example = "Desenvolvedor Backend Sênior")
    private String position;

    @NotNull(message = "A data de início não pode ser nula")
    @Schema(description = "Data de início na empresa", example = "2021-06-01")
    private LocalDate startDate;

    @Schema(description = "Data de saída da empresa", example = "2024-08-31")
    private LocalDate endDate;

    @Schema(description = "Indica se ainda está trabalhando na empresa", example = "true")
    private Boolean currentJob;

    @Size(max = 1000)
    @Schema(description = "Descrição das atividades e responsabilidades",
            example = "Desenvolvimento de APIs REST com Spring Boot, manutenção de microsserviços, implementação de testes automatizados")
    private String description;


}