package com.gs.requalify.controller;

import com.gs.requalify.ai.PromptBuilder;
import com.gs.requalify.dto.RoadmapDTO;
import com.gs.requalify.dto.RoadmapDTOFromAI;
import com.gs.requalify.dto.RoadmapResponseDTO;
import com.gs.requalify.model.Resume;
import com.gs.requalify.model.Roadmap;
import com.gs.requalify.service.RoadmapAIService;
import com.gs.requalify.service.RoadmapService;
import com.gs.requalify.service.ResumeService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final Logger logger = LoggerFactory.getLogger(RoadmapController.class);
    private final RoadmapService roadmapService;
    private final RoadmapAIService roadmapAIService;
    private final ResumeService resumeService;
    private final PromptBuilder promptBuilder;
    private final ObjectMapper objectMapper;

    public RoadmapController(
            RoadmapService roadmapService,
            RoadmapAIService roadmapAIService,
            ResumeService resumeService,
            PromptBuilder promptBuilder,
            ObjectMapper objectMapper) {
        this.roadmapService = roadmapService;
        this.roadmapAIService = roadmapAIService;
        this.resumeService = resumeService;
        this.promptBuilder = promptBuilder;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/user/{userId}")
    @Operation(
            summary = "Criar roadmap com IA",
            description = "Cria um roadmap personalizado analisando o currículo do usuário e objetivo de carreira. A IA gera os checkpoints automaticamente."
    )
    @ApiResponse(responseCode = "201", description = "Roadmap criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário ou currículo não encontrado")
    public ResponseEntity<Object> createRoadmap(
            @Parameter(description = "ID do usuário") @PathVariable Long userId,
            @Valid @RequestBody RoadmapDTO roadmapDTO) {
        try {
            logger.info("Iniciando criação de roadmap para usuário ID: {}, objetivo: {}", userId, roadmapDTO.targetOccupation());

            // Busca o currículo do usuário
            Resume resume = resumeService.getResumeByUserId(userId);

            // Gera o roadmap com checkpoints usando IA (síncrono)
            logger.info("Disparando geração de roadmap com IA...");
            String roadmapJson = roadmapAIService.generateRoadmapWithCheckpoints(
                    resume,
                    roadmapDTO.targetOccupation(),
                    roadmapDTO.description()
            );

            logger.debug("JSON gerado pela IA: {}", roadmapJson);

            // Converte JSON para RoadmapDTOFromAI (desserializa resposta IA)
            RoadmapDTOFromAI roadmapDTOFromAI = objectMapper.readValue(roadmapJson, RoadmapDTOFromAI.class);

            // Converte para RoadmapDTOWithCheckpoints (formato interno)
            RoadmapService.RoadmapDTOWithCheckpoints roadmapWithCheckpoints = roadmapDTOFromAI.toRoadmapDTOWithCheckpoints();

            // Salva no banco de dados
            Roadmap savedRoadmap = roadmapService.createRoadmap(
                    roadmapWithCheckpoints,
                    userId
            );

            RoadmapResponseDTO response = RoadmapResponseDTO.fromEntity(savedRoadmap);
            logger.info("Roadmap criado com sucesso! ID: {}", savedRoadmap.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            logger.error("Currículo não encontrado para usuário ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erro ao criar roadmap: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao gerar roadmap com IA: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar roadmap por ID", description = "Retorna um roadmap com todos seus checkpoints e cursos")
    @ApiResponse(responseCode = "200", description = "Roadmap encontrado")
    @ApiResponse(responseCode = "404", description = "Roadmap não encontrado")
    public ResponseEntity<Object> getRoadmapById(
            @Parameter(description = "ID do roadmap") @PathVariable Long id) {
        try {
            Roadmap roadmap = roadmapService.getRoadmapById(id);
            RoadmapResponseDTO response = RoadmapResponseDTO.fromEntity(roadmap);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Roadmap não encontrado: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Buscar roadmaps do usuário", description = "Retorna todos os roadmaps criados por um usuário")
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
            logger.info("Roadmap deletado: {}", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Roadmap não encontrado: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}