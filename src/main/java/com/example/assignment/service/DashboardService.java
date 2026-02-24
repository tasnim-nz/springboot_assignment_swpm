package com.example.assignment.service;

import com.example.assignment.entity.Course;
import com.example.assignment.entity.Student;
import com.example.assignment.repository.CourseRepository;
import com.example.assignment.repository.StudentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public DashboardService(StudentRepository studentRepository,
                            CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Get students based on user role.
     * ROLE_TEACHER: Returns all students
     * ROLE_STUDENT: Returns only students matching the authenticated username
     * 
     * @return List of students accessible to the current user
     */
    public List<Student> getStudentsForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Robust null and authentication checks
        if (authentication == null || 
            !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            return List.of(); // Return empty list for unauthenticated users
        }

        String loggedInUsername = authentication.getName();
        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            return List.of(); // Safe fallback for null username
        }

        // Check if user has TEACHER role
        boolean isTeacher = authentication.getAuthorities() != null && 
                authentication.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_TEACHER".equals(a.getAuthority()));

        if (isTeacher) {
            return studentRepository.findAll();
        } else {
            // Return students matching the authenticated user's name
            return studentRepository.findByName(loggedInUsername);
        }
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}