package com.college.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.model.Course;
import com.college.repository.CourseRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course>     findAll()             { return courseRepository.findAll(); }
    public List<Course>     findActive()          { return courseRepository.findByActiveTrue(); }
    public Optional<Course> findById(Long id)     { return courseRepository.findById(id); }

    @Transactional
    public Course save(Course course)             { return courseRepository.save(course); }

    @Transactional
    public void   delete(Long id)                 { courseRepository.deleteById(id); }
}
