package com.example.assignment.controller;

import com.example.assignment.entity.Teacher;
import com.example.assignment.service.TeacherService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.findAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        return teacherService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Business Rule: Only TEACHER can create teachers
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) {
        Teacher savedTeacher = teacherService.saveTeacher(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacher);
    }

    @PostMapping("/ui-create")
    @PreAuthorize("hasRole('TEACHER')")
    public void addTeacherViaUI(@RequestParam String name,
                                @RequestParam String teacherId,
                                HttpServletResponse response) throws IOException {
        teacherService.registerFromUI(name, teacherId);
        response.sendRedirect("/dashboard");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, 
                                                 @Valid @RequestBody Teacher teacher) {
        return teacherService.findById(id)
                .map(existingTeacher -> {
                    teacher.setId(id);
                    return ResponseEntity.ok(teacherService.saveTeacher(teacher));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Business Rule: Only TEACHER can delete teachers
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        if (teacherService.findById(id).isPresent()) {
            teacherService.deleteTeacher(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public void deleteTeacherViaUI(@PathVariable Long id, HttpServletResponse response) throws IOException {
        teacherService.deleteTeacher(id);
        response.sendRedirect("/dashboard");
    }
}
