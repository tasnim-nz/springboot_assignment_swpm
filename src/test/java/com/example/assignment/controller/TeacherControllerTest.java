package com.example.assignment.controller;

import com.example.assignment.entity.Teacher;
import com.example.assignment.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(teacherController)
                .build();

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Mr. Smith");
        teacher.setTeacherId("T1001");
    }

    // ✅ Test Get All Teachers
    @Test
    void getAllTeachers() throws Exception {

        when(teacherService.findAllTeachers())
                .thenReturn(Arrays.asList(teacher));

        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mr. Smith"));

        verify(teacherService, times(1)).findAllTeachers();
    }

    // ✅ Test Get Teacher By ID
    @Test
    void getTeacherById() throws Exception {

        when(teacherService.findById(1L))
                .thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/api/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacherId").value("T1001"));

        verify(teacherService, times(1)).findById(1L);
    }

    // ✅ Test Teacher Not Found
    @Test
    void getTeacherById_NotFound() throws Exception {

        when(teacherService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/teachers/99"))
                .andExpect(status().isNotFound());

        verify(teacherService, times(1)).findById(99L);
    }

    // ✅ Test Delete Teacher
    @Test
    void deleteTeacher() throws Exception {

        when(teacherService.findById(1L))
                .thenReturn(Optional.of(teacher));

        doNothing().when(teacherService).deleteTeacher(1L);

        mockMvc.perform(delete("/api/teachers/1"))
                .andExpect(status().isNoContent());

        verify(teacherService, times(1)).deleteTeacher(1L);
    }
}