package com.gs.requalify.controller;

import com.gs.requalify.dto.ResumeDTO;
import com.gs.requalify.dto.ResumeResponseDTO;
import com.gs.requalify.model.Resume;
import com.gs.requalify.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/resume")
@SecurityRequirement(name = "bearerAuth")
public class ResumeController {

    private final ResumeService resumeService;
    Logger log = LoggerFactory.getLogger(ResumeController.class);

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/user/{userId}")
    @Operation(summary = "Criar currículo", description = "Cria um novo currículo para um usuário")
    @ApiResponse(responseCode = "201", description = "Currículo criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário já possui currículo")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Object> createResume(
            @Parameter(description = "ID do usuário") @PathVariable Long userId,
            @Valid @RequestBody ResumeDTO resume) {
        try {
            Resume savedResume = resumeService.createResume(resume, userId);
            ResumeResponseDTO response = ResumeResponseDTO.fromEntity(savedResume);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar currículo por ID", description = "Retorna um currículo pelo ID")
    @ApiResponse(responseCode = "200", description = "Currículo encontrado")
    @ApiResponse(responseCode = "404", description = "Currículo não encontrado")
    public ResponseEntity<Object> getResumeById(
            @Parameter(description = "ID do currículo") @PathVariable Long id) {
        try {
            Resume resume = resumeService.getResumeById(id);
            ResumeResponseDTO response = ResumeResponseDTO.fromEntity(resume);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Buscar currículo por usuário", description = "Retorna o currículo de um usuário")
    @ApiResponse(responseCode = "200", description = "Currículo encontrado")
    @ApiResponse(responseCode = "404", description = "Currículo não encontrado")
    public ResponseEntity<Object> getResumeByUserId(
            @Parameter(description = "ID do usuário") @PathVariable Long userId) {
        try {
            Resume resume = resumeService.getResumeByUserId(userId);
            ResumeResponseDTO response = ResumeResponseDTO.fromEntity(resume);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar currículo", description = "Atualiza um currículo existente")
    @ApiResponse(responseCode = "200", description = "Currículo atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Currículo não encontrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<Object> updateResume(
            @Parameter(description = "ID do currículo") @PathVariable Long id,
            @Valid @RequestBody ResumeDTO resume) {
        try {
            Resume updatedResume = resumeService.updateResume(id, resume);
            ResumeResponseDTO response = ResumeResponseDTO.fromEntity(updatedResume);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar currículo", description = "Remove um currículo do sistema")
    @ApiResponse(responseCode = "204", description = "Currículo deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Currículo não encontrado")
    public ResponseEntity<Object> deleteResume(
            @Parameter(description = "ID do currículo") @PathVariable Long id) {
        try {
            resumeService.deleteResume(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}