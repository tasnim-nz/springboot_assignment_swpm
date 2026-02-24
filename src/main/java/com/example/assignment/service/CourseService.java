package com.example.assignment.service;

import com.example.assignment.entity.Course;
import com.example.assignment.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void addCourseFromUI(String title, String courseCode) {
        Course newCourse = new Course();
        newCourse.setTitle(title);
        newCourse.setCourseCode(courseCode);
        courseRepository.save(newCourse);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }
}
