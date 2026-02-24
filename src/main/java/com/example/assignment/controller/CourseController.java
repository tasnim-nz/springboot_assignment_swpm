package com.example.assignment.controller;

import com.example.assignment.entity.Course;
import com.example.assignment.service.CourseService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.findAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Business Rule: Only TEACHER can create courses (students cannot create courses)
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        Course savedCourse = courseService.saveCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @PostMapping("/ui-create")
    @PreAuthorize("hasRole('TEACHER')")
    public void createCourseViaUI(@RequestParam String title,
                                  @RequestParam String courseCode,
                                  HttpServletResponse response) throws IOException {
        courseService.addCourseFromUI(title, courseCode);
        response.sendRedirect("/dashboard");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, 
                                               @Valid @RequestBody Course course) {
        return courseService.findById(id)
                .map(existingCourse -> {
                    course.setId(id);
                    return ResponseEntity.ok(courseService.saveCourse(course));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (courseService.findById(id).isPresent()) {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public void deleteCourseViaUI(@PathVariable Long id, HttpServletResponse response) throws IOException {
        courseService.deleteCourse(id);
        response.sendRedirect("/dashboard");
    }
}
