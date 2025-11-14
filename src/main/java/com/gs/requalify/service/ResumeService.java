package com.gs.requalify.service;

import com.gs.requalify.dto.ResumeDTO;
import com.gs.requalify.model.*;
import com.gs.requalify.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Resume createResume(ResumeDTO resumeDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (resumeRepository.existsByUserId(userId)) throw new RuntimeException("Usuário já possui um currículo");

        Resume resume = Resume.builder()
                .user(user)
                .occupation(resumeDTO.occupation())
                .summary(resumeDTO.summary())
                .skills(resumeDTO.skills())
                .educations(resumeDTO.educations())
                .experiences(resumeDTO.experiences())
                .certifications(resumeDTO.certifications())
                .build();

        if (resume.getEducations() != null) resume.getEducations().forEach(education -> education.setResume(resume));
        if (resume.getExperiences() != null) resume.getExperiences().forEach(experience -> experience.setResume(resume));
        if (resume.getCertifications() != null) resume.getCertifications().forEach(certification -> certification.setResume(resume));

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
    public Resume updateResume(Long id, ResumeDTO resumeDTO) {
        Resume resumeExistente = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado"));

        resumeExistente.setOccupation(resumeDTO.occupation());
        resumeExistente.setSummary(resumeDTO.summary());
        resumeExistente.setSkills(resumeDTO.skills());

        if (resumeDTO.educations() != null) {
            if (resumeExistente.getEducations() != null) resumeExistente.getEducations().clear();

            resumeDTO.educations().forEach(education -> {
                education.setResume(resumeExistente);
                resumeExistente.getEducations().add(education);
            });
        }

        if (resumeDTO.experiences() != null) {
            if (resumeExistente.getExperiences() != null) resumeExistente.getExperiences().clear();

            resumeDTO.experiences().forEach(experience -> {
                experience.setResume(resumeExistente);
                resumeExistente.getExperiences().add(experience);
            });
        }

        if (resumeDTO.certifications() != null) {
            if (resumeExistente.getCertifications() != null) resumeExistente.getCertifications().clear();

            resumeDTO.certifications().forEach(certification -> {
                certification.setResume(resumeExistente);
                resumeExistente.getCertifications().add(certification);
            });
        }

        return resumeRepository.save(resumeExistente);
    }

    @Transactional
    @CacheEvict(value = "resumes", allEntries = true)
    public void deleteResume(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currículo não encontrado"));

        User user = resume.getUser();
        if (user != null) {
            user.setResume(null);
            userRepository.save(user);
        }

        resumeRepository.delete(resume);
    }
}