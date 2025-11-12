package com.gs.requalify.repository;

import com.gs.requalify.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByResumeId(Long resumeId);
}