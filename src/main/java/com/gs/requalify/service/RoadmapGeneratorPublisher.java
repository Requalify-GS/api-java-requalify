package com.gs.requalify.service;

import com.gs.requalify.model.Resume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.gs.requalify.config.RabbitConfig.ROADMAP_QUEUE;

@Service
public class RoadmapGeneratorPublisher {

    private static final Logger logger = LoggerFactory.getLogger(RoadmapGeneratorPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public RoadmapGeneratorPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendResumeForRoadmap(Resume resume) {
        try {
            rabbitTemplate.convertAndSend(ROADMAP_QUEUE, resume.getId());
            logger.info("Mensagem enviada para fila: {} com currículo ID: {}", ROADMAP_QUEUE, resume.getId());
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem para fila: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao publicar tarefa de geração de roadmap", e);
        }
    }
}