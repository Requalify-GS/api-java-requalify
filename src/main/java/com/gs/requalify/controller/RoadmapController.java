package com.gs.requalify.controller;

import com.gs.requalify.dto.RoadmapDTO;
import com.gs.requalify.dto.RoadmapResponseDTO;
import com.gs.requalify.model.Roadmap;
import com.gs.requalify.service.RoadmapService;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roadmap")
@SecurityRequirement(name = "bearerAuth")
public class RoadmapController {

    private final RoadmapService roadmapService;
    Logger log = LoggerFactory.getLogger(RoadmapController.class);

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @PostMapping("/user/{userId}")
    @Operation(summary = "Criar roadmap", description = "Cria um novo roadmap para um usuário")
    @ApiResponse(responseCode = "201", description = "Roadmap criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Object> createRoadmap(
            @Parameter(description = "ID do usuário") @PathVariable Long userId,
            @Valid @RequestBody RoadmapDTO roadmapDTO) {
        try {
            Roadmap savedRoadmap = roadmapService.createRoadmap(roadmapDTO, userId);
            RoadmapResponseDTO response = RoadmapResponseDTO.fromEntity(savedRoadmap);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar roadmap por ID", description = "Retorna um roadmap pelo ID")
    @ApiResponse(responseCode = "200", description = "Roadmap encontrado")
    @ApiResponse(responseCode = "404", description = "Roadmap não encontrado")
    public ResponseEntity<Object> getRoadmapById(
            @Parameter(description = "ID do roadmap") @PathVariable Long id) {
        try {
            Roadmap roadmap = roadmapService.getRoadmapById(id);
            RoadmapResponseDTO response = RoadmapResponseDTO.fromEntity(roadmap);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Buscar roadmaps por usuário", description = "Retorna todos os roadmaps de um usuário")
    @ApiResponse(responseCode = "200", description = "Roadmaps encontrados")
    public ResponseEntity<List<RoadmapResponseDTO>> getRoadmapsByUserId(
            @Parameter(description = "ID do usuário") @PathVariable Long userId) {
        List<Roadmap> roadmaps = roadmapService.getRoadmapsByUserId(userId);
        List<RoadmapResponseDTO> response = roadmaps.stream()
                .map(RoadmapResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar roadmap", description = "Remove um roadmap do sistema")
    @ApiResponse(responseCode = "204", description = "Roadmap deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Roadmap não encontrado")
    public ResponseEntity<Object> deleteRoadmap(
            @Parameter(description = "ID do roadmap") @PathVariable Long id) {
        try {
            roadmapService.deleteRoadmap(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}