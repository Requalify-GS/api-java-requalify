package com.gs.requalify.service;

import com.gs.requalify.dto.ResumeDTO;
import com.gs.requalify.model.*;
import com.gs.requalify.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository,
                         UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @CacheEvict(value = "resumes", allEntries = true)
    public Resume createResume(ResumeDTO dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (resumeRepository.existsByUserId(userId))
            throw new RuntimeException("Usuário já possui um currículo");

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setOccupation(dto.occupation());
        resume.setSummary(dto.summary());
        resume.setSkills(dto.skills());

        // criar listas mutáveis
        resume.setEducations(new ArrayList<>());
        resume.setExperiences(new ArrayList<>());
        resume.setCertifications(new ArrayList<>());

        if (dto.educations() != null) {
            dto.educations().forEach(education -> {
                education.setResume(resume);
                resume.getEducations().add(education);
            });
        }

        if (dto.experiences() != null) {
            dto.experiences().forEach(experience -> {
                experience.setResume(resume);
                resume.getExperiences().add(experience);
            });
        }

        if (dto.certifications() != null) {
            dto.certifications().forEach(certification -> {
                certification.setResume(resume);
                resume.getCertifications().add(certification);
            });
        }

        return resumeRepository.save(resume);
    }

    @Cacheable(value = "resumes", key = "#id")
    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado"));
    }

    @Cacheable(value = "resumes", key = "'user-' + #userId")
    public Resume getResumeByUserId(Long userId) {
        return resumeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado para este usuário"));
    }

    @Transactional
    @CacheEvict(value = "resumes", allEntries = true)
    public Resume updateResume(Long id, ResumeDTO dto) {

        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado"));

        resume.setOccupation(dto.occupation());
        resume.setSummary(dto.summary());
        resume.setSkills(dto.skills());

        resume.getEducations().clear();
        if (dto.educations() != null) {
            dto.educations().forEach(education -> {
                education.setResume(resume);
                resume.getEducations().add(education);
            });
        }

        resume.getExperiences().clear();
        if (dto.experiences() != null) {
            dto.experiences().forEach(experience -> {
                experience.setResume(resume);
                resume.getExperiences().add(experience);
            });
        }

        resume.getCertifications().clear();
        if (dto.certifications() != null) {
            dto.certifications().forEach(certification -> {
                certification.setResume(resume);
                resume.getCertifications().add(certification);
            });
        }

        return resumeRepository.save(resume);
    }

    @Transactional
    @CacheEvict(value = "resumes", allEntries = true)
    public void deleteResume(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado"));

        resumeRepository.delete(resume);
    }
}