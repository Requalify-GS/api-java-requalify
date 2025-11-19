package com.gs.requalify.service;

import com.gs.requalify.dto.CheckpointDTO;
import com.gs.requalify.dto.CourseDTO;
import com.gs.requalify.model.Checkpoint;
import com.gs.requalify.model.Course;
import com.gs.requalify.model.Roadmap;
import com.gs.requalify.model.User;
import com.gs.requalify.repository.RoadmapRepository;
import com.gs.requalify.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoadmapService {

    private static final Logger logger = LoggerFactory.getLogger(RoadmapService.class);
    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;

    public RoadmapService(RoadmapRepository roadmapRepository, UserRepository userRepository) {
        this.roadmapRepository = roadmapRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Roadmap createRoadmap(RoadmapDTOWithCheckpoints dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + userId));

        Roadmap roadmap = Roadmap.builder()
                .user(user)
                .targetOccupation(dto.targetOccupation)
                .description(dto.description)
                .build();

        if (dto.checkpoints != null) {
            roadmap.setCheckpoints(
                    dto.checkpoints.stream()
                            .map(checkpointDTO -> mapCheckpoint(checkpointDTO, roadmap))
                            .collect(Collectors.toList())
            );
        }

        Roadmap saved = roadmapRepository.save(roadmap);
        logger.info("Roadmap criado com sucesso para usuário ID: {}", userId);
        return saved;
    }

    public Roadmap getRoadmapById(Long id) {
        return roadmapRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Roadmap não encontrado: " + id));
    }

    public List<Roadmap> getRoadmapsByUserId(Long userId) {
        return roadmapRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteRoadmap(Long id) {
        Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Roadmap não encontrado: " + id));
        roadmapRepository.delete(roadmap);
        logger.info("Roadmap deletado: {}", id);
    }


    private Checkpoint mapCheckpoint(CheckpointDTO dto, Roadmap roadmap) {
        Checkpoint checkpoint = Checkpoint.builder()
                .roadmap(roadmap)
                .title(dto.title())
                .description(dto.description())
                .order(dto.order())
                .build();

        if (dto.courses() != null) {
            checkpoint.setCourses(
                    dto.courses().stream()
                            .map(courseDTO -> mapCourse(courseDTO, checkpoint))
                            .collect(Collectors.toList())
            );
        }

        return checkpoint;
    }

    private Course mapCourse(CourseDTO dto, Checkpoint checkpoint) {
        return Course.builder()
                .checkpoint(checkpoint)
                .name(dto.name())
                .platform(dto.platform())
                .url(dto.url())
                .description(dto.description())
                .durationHours(dto.durationHours())
                .build();
    }

    // DTO interno para representar um roadmap com checkpoints
    public static class RoadmapDTOWithCheckpoints {
        public final String targetOccupation;
        public final String description;
        public final List<CheckpointDTO> checkpoints;

        public RoadmapDTOWithCheckpoints(String targetOccupation, String description, List<CheckpointDTO> checkpoints) {
            this.targetOccupation = targetOccupation;
            this.description = description;
            this.checkpoints = checkpoints;
        }
    }
}