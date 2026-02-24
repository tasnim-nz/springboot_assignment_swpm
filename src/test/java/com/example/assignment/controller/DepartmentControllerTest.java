package com.example.assignment.controller;

import com.example.assignment.entity.Department;
import com.example.assignment.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private Department department;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();

        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
    }

    @Test
    void getAllDepartments() throws Exception {

        when(departmentService.getAllDepartments())
                .thenReturn(Arrays.asList(department));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Computer Science"));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    void getDepartmentById() throws Exception {

        when(departmentService.findById(1L))
                .thenReturn(Optional.of(department));

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Computer Science"));

        verify(departmentService, times(1)).findById(1L);
    }

    @Test
    void getDepartmentById_NotFound() throws Exception {

        when(departmentService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/departments/99"))
                .andExpect(status().isNotFound());

        verify(departmentService, times(1)).findById(99L);
    }
}