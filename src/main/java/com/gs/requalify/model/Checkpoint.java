package com.gs.requalify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de checkpoint de um roadmap")
public class Checkpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do checkpoint", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roadmap_id", nullable = false)
    @NotNull(message = "O roadmap não pode ser nulo")
    @Schema(description = "Roadmap associado")
    @JsonIgnore
    private Roadmap roadmap;

    @NotBlank(message = "O título não pode ser nulo")
    @Size(min = 3, max = 200, message = "O título deve ter entre 3 e 200 caracteres")
    @Schema(description = "Título do checkpoint", example = "Fundamentos de JavaScript")
    private String title;

    @NotBlank(message = "A descrição não pode ser nula")
    @Size(min = 10, max = 1000, message = "A descrição deve ter entre 10 e 1000 caracteres")
    @Schema(description = "Descrição detalhada do checkpoint")
    private String description;

    @NotNull(message = "A ordem não pode ser nula")
    @Positive(message = "A ordem deve ser um número positivo")
    @Column(name = "checkpoint_order")
    @Schema(description = "Ordem do checkpoint no roadmap", example = "1")
    private Integer order;

    @OneToMany(mappedBy = "checkpoint", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de cursos relacionados ao checkpoint")
    private List<Course> courses = new ArrayList<>();
}