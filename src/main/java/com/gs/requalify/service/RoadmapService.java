package com.gs.requalify.service;

import com.gs.requalify.dto.RoadmapDTO;
import com.gs.requalify.model.*;
import com.gs.requalify.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;

    public RoadmapService(RoadmapRepository roadmapRepository,
                          UserRepository userRepository) {
        this.roadmapRepository = roadmapRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @CacheEvict(value = "roadmaps", allEntries = true)
    public Roadmap createRoadmap(RoadmapDTO roadmapDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Roadmap roadmap = Roadmap.builder()
                .user(user)
                .targetOccupation(roadmapDTO.targetOccupation())
                .description(roadmapDTO.description())
                .build();

        if (roadmapDTO.checkpoints() != null) {
            List<Checkpoint> checkpoints = roadmapDTO.checkpoints().stream()
                    .map(checkpointDTO -> {
                        Checkpoint checkpoint = Checkpoint.builder()
                                .roadmap(roadmap)
                                .title(checkpointDTO.title())
                                .description(checkpointDTO.description())
                                .order(checkpointDTO.order())
                                .build();

                        if (checkpointDTO.courses() != null) {
                            List<Course> courses = checkpointDTO.courses().stream()
                                    .map(courseDTO -> Course.builder()
                                            .checkpoint(checkpoint)
                                            .name(courseDTO.name())
                                            .platform(courseDTO.platform())
                                            .url(courseDTO.url())
                                            .description(courseDTO.description())
                                            .durationHours(courseDTO.durationHours())
                                            .build())
                                    .collect(Collectors.toList());
                            checkpoint.setCourses(courses);
                        }

                        return checkpoint;
                    })
                    .collect(Collectors.toList());
            roadmap.setCheckpoints(checkpoints);
        }

        return roadmapRepository.save(roadmap);
    }

    @Cacheable(value = "roadmaps", key = "#id")
    public Roadmap getRoadmapById(Long id) {
        return roadmapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Roadmap não encontrado"));
    }

    @Cacheable(value = "roadmaps", key = "'user-' + #userId")
    public List<Roadmap> getRoadmapsByUserId(Long userId) {
        return roadmapRepository.findByUserId(userId);
    }

    @Transactional
    @CacheEvict(value = "roadmaps", allEntries = true)
    public void deleteRoadmap(Long id) {
        if (!roadmapRepository.existsById(id)) {
            throw new RuntimeException("Roadmap não encontrado");
        }
        roadmapRepository.deleteById(id);
    }
}