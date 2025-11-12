package com.gs.requalify.repository;

import com.gs.requalify.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByResumeId(Long resumeId);
}