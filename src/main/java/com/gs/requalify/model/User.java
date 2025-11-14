package com.gs.requalify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
@Schema(description = "Modelo de usuário")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do usuário", example = "2")
    private Long id;

    @NotBlank(message = "O e-mail do usuário não pode ser nulo")
    @Email(message = "O e-mail do usuário deve ser válido")
    @Schema(description = "Email do usuário", example = "alves@gmail.com")
    private String username;

    @NotBlank(message = "A senha do usuário não pode ser nula")
    @Size(min = 1, max = 100)
    @Schema(description = "Senha do usuário", example = "123456")
    private String password;

    @NotBlank(message = "O nome do usuário não pode ser nulo")
    @Size(min = 1, max = 255)
    @Schema(description = "Nome completo do usuário", example = "Guilherme Alves")
    private String name;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Currículo do usuário")
    @JsonIgnore
    private Resume resume;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Roadmaps do usuário")
    @JsonIgnore
    private List<Roadmap> roadmaps;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

}
