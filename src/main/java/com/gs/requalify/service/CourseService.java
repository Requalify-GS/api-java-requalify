package com.gs.requalify.service;

import com.gs.requalify.dto.CourseDTO;
import com.gs.requalify.model.Checkpoint;
import com.gs.requalify.model.Course;
import com.gs.requalify.repository.CheckpointRepository;
import com.gs.requalify.repository.CourseRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CheckpointRepository checkpointRepository;

    public CourseService(CourseRepository courseRepository, CheckpointRepository checkpointRepository) {
        this.courseRepository = courseRepository;
        this.checkpointRepository = checkpointRepository;
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public Course createCourse(CourseDTO courseDTO, Long checkpointId) {
        Checkpoint checkpoint = checkpointRepository.findById(checkpointId)
                .orElseThrow(() -> new RuntimeException("Checkpoint não encontrado"));

        Course course = Course.builder()
                .checkpoint(checkpoint)
                .name(courseDTO.name())
                .platform(courseDTO.platform())
                .url(courseDTO.url())
                .description(courseDTO.description())
                .durationHours(courseDTO.durationHours())
                .build();

        return courseRepository.save(course);
    }

    @Cacheable(value = "courses", key = "#id")
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
    }

    public List<Course> getCoursesByCheckpointId(Long checkpointId) {
        return courseRepository.findByCheckpointId(checkpointId);
    }

    public Page<Course> getAllCoursesPaginated(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public Course updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        course.setName(courseDTO.name());
        course.setPlatform(courseDTO.platform());
        course.setUrl(courseDTO.url());
        course.setDescription(courseDTO.description());
        course.setDurationHours(courseDTO.durationHours());

        return courseRepository.save(course);
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Curso não encontrado");
        }
        courseRepository.deleteById(id);
    }
}