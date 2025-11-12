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

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de currículo")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do currículo", example = "2")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @NotNull(message = "O usuário não pode ser nulo")
    @Schema(description = "Usuário dono do currículo")
    @JsonIgnore
    private User user;

    @NotBlank(message = "A profissão do usuário não pode ser nula")
    @Size(min=3, max=150, message = "A profissão deve ter entre 3 e 150 caracteres")
    @Schema(description = "Profissão do usuário", example = "Desenvolvedor Java")
    private String occupation;

    @NotBlank(message = "O resumo profissional é obrigatório")
    @Size(min = 50, max = 1000, message = "O resumo deve ter entre 50 e 1000 caracteres")
    private String summary;

    @ElementCollection
    @Size(max = 20, message = "Máximo de 20 skills permitidas")
    private List<@NotBlank(message = "Skill não pode estar vazia") String> skills;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de formações educacionais")
    private List<Education> educations;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de experiências profissionais")
    private List<Experience> experiences;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de certificações")
    private List<Certification> certifications;
}