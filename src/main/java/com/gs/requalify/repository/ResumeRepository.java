package com.gs.requalify.repository;

import com.gs.requalify.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}