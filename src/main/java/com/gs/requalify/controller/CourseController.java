package com.gs.requalify.controller;

import com.gs.requalify.dto.CourseDTO;
import com.gs.requalify.dto.CourseResponseDTO;
import com.gs.requalify.model.Course;
import com.gs.requalify.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
@SecurityRequirement(name = "bearerAuth")
public class CourseController {

    private final CourseService courseService;
    Logger log = LoggerFactory.getLogger(CourseController.class);

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/checkpoint/{checkpointId}")
    @Operation(summary = "Criar curso", description = "Cria um novo curso para um checkpoint")
    @ApiResponse(responseCode = "201", description = "Curso criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Checkpoint não encontrado")
    public ResponseEntity<Object> createCourse(
            @Parameter(description = "ID do checkpoint") @PathVariable Long checkpointId,
            @Valid @RequestBody CourseDTO courseDTO) {
        try {
            Course savedCourse = courseService.createCourse(courseDTO, checkpointId);
            CourseResponseDTO response = CourseResponseDTO.fromEntity(savedCourse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar curso por ID", description = "Retorna um curso pelo ID")
    @ApiResponse(responseCode = "200", description = "Curso encontrado")
    @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    public ResponseEntity<Object> getCourseById(
            @Parameter(description = "ID do curso") @PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            CourseResponseDTO response = CourseResponseDTO.fromEntity(course);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/checkpoint/{checkpointId}")
    @Operation(summary = "Buscar cursos por checkpoint", description = "Retorna todos os cursos de um checkpoint")
    @ApiResponse(responseCode = "200", description = "Cursos encontrados")
    public ResponseEntity<List<CourseResponseDTO>> getCoursesByCheckpointId(
            @Parameter(description = "ID do checkpoint") @PathVariable Long checkpointId) {
        List<Course> courses = courseService.getCoursesByCheckpointId(checkpointId);
        List<CourseResponseDTO> response = courses.stream()
                .map(CourseResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Listar todos os cursos com paginação",
            description = "Retorna todos os cursos do sistema com suporte a paginação")
    @ApiResponse(responseCode = "200", description = "Cursos encontrados")
    public ResponseEntity<Page<CourseResponseDTO>> getAllCoursesPaginated(
            @Parameter(description = "Número da página (inicia em 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseService.getAllCoursesPaginated(pageable);
        Page<CourseResponseDTO> response = courses.map(CourseResponseDTO::fromEntity);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar curso", description = "Atualiza um curso existente")
    @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<Object> updateCourse(
            @Parameter(description = "ID do curso") @PathVariable Long id,
            @Valid @RequestBody CourseDTO courseDTO) {
        try {
            Course updatedCourse = courseService.updateCourse(id, courseDTO);
            CourseResponseDTO response = CourseResponseDTO.fromEntity(updatedCourse);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar curso", description = "Remove um curso do sistema")
    @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    public ResponseEntity<Object> deleteCourse(
            @Parameter(description = "ID do curso") @PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}