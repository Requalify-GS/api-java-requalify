package com.gs.requalify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gs.requalify.service.RoadmapService;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// Desserializar o JSON retornado pelo GPT
@Schema(description = "DTO para desserializar resposta da IA com checkpoints")
public record RoadmapDTOFromAI(
        @JsonProperty("targetOccupation")
        @Schema(description = "Profissão objetivo", example = "Desenvolvedor Full Stack")
        String targetOccupation,

        @JsonProperty("description")
        @Schema(description = "Descrição do roadmap")
        String description,

        @JsonProperty("checkpoints")
        @Schema(description = "Lista de checkpoints gerados pela IA")
        List<CheckpointDTO> checkpoints
) {

    public RoadmapService.RoadmapDTOWithCheckpoints toRoadmapDTOWithCheckpoints() {
        return new RoadmapService.RoadmapDTOWithCheckpoints(
                this.targetOccupation(),
                this.description(),
                this.checkpoints()
        );
    }
}