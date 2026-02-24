package com.example.assignment.controller;

import com.example.assignment.entity.Student;
import com.example.assignment.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student student;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setStudentId("ST1001");
    }

    // ✅ Test GET all students
    @Test
    void getAllStudents() throws Exception {

        when(studentService.findAllStudents())
                .thenReturn(Arrays.asList(student));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(studentService, times(1)).findAllStudents();
    }

    // ✅ Test GET student by id
    @Test
    void getStudentById() throws Exception {

        when(studentService.findById(1L))
                .thenReturn(Optional.of(student));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("ST1001"));

        verify(studentService, times(1)).findById(1L);
    }

    // ✅ Test Not Found
    @Test
    void getStudentById_NotFound() throws Exception {

        when(studentService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).findById(99L);
    }

    // ✅ Test Delete
    @Test
    void deleteStudent() throws Exception {

        when(studentService.findById(1L))
                .thenReturn(Optional.of(student));

        doNothing().when(studentService).removeStudent(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).removeStudent(1L);
    }
}