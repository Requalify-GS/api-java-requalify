package com.gs.requalify.service;

import com.gs.requalify.ai.PromptBuilder;
import com.gs.requalify.model.Resume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RoadmapAIService {

    private static final Logger logger = LoggerFactory.getLogger(RoadmapAIService.class);
    private final ChatClient chatClient;
    private final PromptBuilder promptBuilder;

    public RoadmapAIService(ChatClient chatClient, PromptBuilder promptBuilder) {
        this.chatClient = chatClient;
        this.promptBuilder = promptBuilder;
    }

    public String generateRoadmapWithCheckpoints(Resume resume, String targetOccupation, String description) {

        String initialPrompt = promptBuilder.buildRoadmapPrompt(resume, targetOccupation, description);

        String r1 = generateRoadmap(initialPrompt);
        logger.info("Passo 1 concluído (geração inicial)");

        String r2 = refineRoadmap(r1, targetOccupation);
        logger.info("Passo 2 concluído (primeiro refinamento)");

        String r3 = refineRoadmap(r2, targetOccupation);
        logger.info("Passo 3 concluído (segundo refinamento)");

        // Limpa e valida JSON antes de retornar
        String cleanJson = cleanJsonResponse(r3);
        return cleanJson;
    }

    public String generateRoadmap(String prompt) {
        try {
            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
            return response;
        } catch (Exception e) {
            logger.error("Erro ao gerar roadmap: {}", e.getMessage(), e);
            throw new RuntimeException("Falha na geração do roadmap via IA", e);
        }
    }

    public String refineRoadmap(String roadmapJson, String targetOccupation) {
        String refinePrompt = """
            Reavalie o roadmap abaixo e melhore sua clareza, completude e organização.
            O roadmap deve permanecer NO FORMATO JSON FINAL, sem nenhum texto extra.

            Profissão objetivo: %s

            Roadmap atual:
            %s

            Melhore o roadmap seguindo:
            - Corrigir erros de JSON
            - Refinar descrições dos checkpoints
            - Otimizar sugestões de cursos (usar plataformas reais)
            - Manter estrutura JSON válida
            - Manter exatamente o formato esperado
            - NUNCA inclua markdown, código ou caracteres especiais fora do JSON
            """.formatted(targetOccupation, roadmapJson);

        return generateRoadmap(refinePrompt);
    }


    // Remoção dos caracteres invalidos do JSON, retornado pelo GPT - Mais especificamente ```
    private String cleanJsonResponse(String response) {
        if (response == null || response.isEmpty()) {
            throw new RuntimeException("Resposta vazia da IA");
        }
        response = response.trim();

        if (response.startsWith("```json")) {
            response = response.substring(7);
        } else if (response.startsWith("```")) {
            response = response.substring(3);
        }

        if (response.endsWith("```")) {
            response = response.substring(0, response.length() - 3);
        }
        response = response.trim();
        return response;
    }
}