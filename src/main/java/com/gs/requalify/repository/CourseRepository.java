package com.gs.requalify.repository;

import com.gs.requalify.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCheckpointId(Long checkpointId);
}