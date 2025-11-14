package com.gs.requalify.repository;

import com.gs.requalify.model.Checkpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckpointRepository extends JpaRepository<Checkpoint, Long> {
    List<Checkpoint> findByRoadmapIdOrderByOrderAsc(Long roadmapId);
}
