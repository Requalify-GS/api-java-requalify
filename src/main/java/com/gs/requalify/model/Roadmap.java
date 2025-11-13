package com.gs.requalify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Modelo de roadmap de carreira")
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do roadmap", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "O usuário não pode ser nulo")
    @Schema(description = "Usuário dono do roadmap")
    @JsonIgnore
    private User user;

    @NotBlank(message = "A profissão objetivo não pode ser nula")
    @Size(min = 3, max = 150, message = "A profissão deve ter entre 3 e 150 caracteres")
    @Schema(description = "Profissão objetivo do roadmap", example = "Desenvolvedor Full Stack")
    private String targetOccupation;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    @Schema(description = "Descrição do roadmap")
    private String description;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de checkpoints do roadmap")
    private List<Checkpoint> checkpoints = new ArrayList<>();
}