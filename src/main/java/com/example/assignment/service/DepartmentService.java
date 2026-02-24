package com.example.assignment.service;

import com.example.assignment.entity.Department;
import com.example.assignment.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department saveDepartment(Department department) {
        // Example Logic: Force department name to uppercase
        if (department.getName() != null) {
            department.setName(department.getName().toUpperCase());
        }
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return departmentRepository.existsById(id);
    }
}
