package com.gs.requalify.ai;

import com.gs.requalify.model.Resume;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PromptBuilder {

    public String buildRoadmapPrompt(Resume resume, String targetOccupation, String description) {
        String skills = buildSkillsString(resume);
        String experiences = buildExperiencesString(resume);
        String educations = buildEducationsString(resume);
        String certifications = buildCertificationsString(resume);

        return """
            Você é um especialista em desenvolvimento de carreira e planejamento profissional.
            Analise o currículo abaixo e crie um roadmap personalizado para a transição de carreira.

            **CURRÍCULO ATUAL:**
            Profissão atual: %s
            Resumo: %s
            Skills: %s
            Experiências: %s
            Formações: %s
            Certificações: %s

            **OBJETIVO DE CARREIRA:**
            Profissão: %s
            Descrição: %s

            **TAREFA:**
            Crie um roadmap estruturado em JSON com a seguinte estrutura EXATA:
            {
              "targetOccupation": "string",
              "description": "string",
              "checkpoints": [
                {
                  "title": "string",
                  "description": "string",
                  "order": number,
                  "courses": [
                    {
                      "name": "string",
                      "platform": "string",
                      "url": "string",
                      "description": "string",
                      "durationHours": number
                    }
                  ]
                }
              ]
            }

            **INSTRUÇÕES CRÍTICAS:**
            - Considere as habilidades que o profissional já possui
            - Identifique as lacunas de conhecimento
            - Sugira de 4 a 7 checkpoints ordenados logicamente
            - Cada checkpoint deve ter 2–4 cursos reais
            - Use plataformas reais: Udemy, Coursera, LinkedIn Learning, A Cloud Guru, Alura, etc
            - As URLs devem ser URLs plausíveis (não precisam ser válidas, mas realistas)
            - durationHours deve ser um número inteiro positivo
            - order deve ser sequencial começando em 1
            - Retorne APENAS o JSON, sem nenhum texto adicional, markdown ou explicações
            - O JSON deve ser válido e completo
            """.formatted(
                resume.getOccupation(),
                resume.getSummary(),
                skills,
                experiences,
                educations,
                certifications,
                targetOccupation,
                description
        );
    }


    private String buildSkillsString(Resume resume) {
        if (resume.getSkills() == null || resume.getSkills().isEmpty()) {
            return "Não informado";
        }
        return resume.getSkills().stream()
                .collect(Collectors.joining(", "));
    }

    private String buildExperiencesString(Resume resume) {
        if (resume.getExperiences() == null || resume.getExperiences().isEmpty()) {
            return "Não informado";
        }
        return resume.getExperiences().stream()
                .map(e -> e.getPosition() + " em " + e.getCompany() +
                        (e.getDescription() != null ? " (" + e.getDescription() + ")" : ""))
                .collect(Collectors.joining("; "));
    }

    private String buildEducationsString(Resume resume) {
        if (resume.getEducations() == null || resume.getEducations().isEmpty()) {
            return "Não informado";
        }
        return resume.getEducations().stream()
                .map(e -> e.getCourse() + " - " + e.getInstitution() + " (" + e.getEducationLevel() + ")")
                .collect(Collectors.joining("; "));
    }

    private String buildCertificationsString(Resume resume) {
        if (resume.getCertifications() == null || resume.getCertifications().isEmpty()) {
            return "Não informado";
        }
        return resume.getCertifications().stream()
                .map(c -> c.getName() + " - " + c.getIssuingOrganization())
                .collect(Collectors.joining("; "));
    }
}