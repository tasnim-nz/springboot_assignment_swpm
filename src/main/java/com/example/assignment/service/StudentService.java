package com.example.assignment.service;

import com.example.assignment.entity.Student;
import com.example.assignment.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void registerFromUI(String name, String studentId) {
        Student student = new Student();
        student.setName(name);
        student.setStudentId(studentId);
        studentRepository.save(student);
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public void removeStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findByStudentName(String name) {
        return studentRepository.findByName(name);
    }

    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }
}
