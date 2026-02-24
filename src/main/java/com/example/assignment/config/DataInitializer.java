package com.example.assignment.config;

import com.example.assignment.entity.*;
import com.example.assignment.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository,
            CourseRepository courseRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            // Create users if they don't exist
            if (!userRepository.existsByUsername("student1")) {
                User student = new User("student1", passwordEncoder.encode("pass123"), "ROLE_STUDENT");
                userRepository.save(student);
                System.out.println("Created student user: student1/pass123");
            }

            if (!userRepository.existsByUsername("teacher1")) {
                User teacher = new User("teacher1", passwordEncoder.encode("pass123"), "ROLE_TEACHER");
                userRepository.save(teacher);
                System.out.println("Created teacher user: teacher1/pass123");
            }

            if (!userRepository.existsByUsername("student2")) {
                User student2 = new User("student2", passwordEncoder.encode("pass123"), "ROLE_STUDENT");
                userRepository.save(student2);
                System.out.println("Created student user: student2/pass123");
            }

            // Create sample data if database is empty
            if (departmentRepository.count() == 0) {
                Department csDept = new Department();
                csDept.setName("Computer Science");
                departmentRepository.save(csDept);

                Department mathDept = new Department();
                mathDept.setName("Mathematics");
                departmentRepository.save(mathDept);

                System.out.println("Created sample departments");
            }

            if (studentRepository.count() == 0 && departmentRepository.count() > 0) {
                Department dept = departmentRepository.findAll().get(0);
                
                Student student = new Student();
                student.setName("John Doe");
                student.setStudentId("2107081");
                student.setDepartment(dept);
                studentRepository.save(student);

                System.out.println("Created sample student");
            }

            if (teacherRepository.count() == 0 && departmentRepository.count() > 0) {
                Department dept = departmentRepository.findAll().get(0);
                
                Teacher teacher = new Teacher();
                teacher.setName("Dr. Smith");
                teacher.setTeacherId("T001");
                teacher.setEmail("smith@example.com");
                teacher.setDepartment(dept);
                teacherRepository.save(teacher);

                System.out.println("Created sample teacher");
            }

            System.out.println("\n========================================");
            System.out.println("Database initialized successfully!");
            System.out.println("========================================");
            System.out.println("Test accounts:");
            System.out.println("  Student: student1 / pass123");
            System.out.println("  Student: student2 / pass123");
            System.out.println("  Teacher: teacher1 / pass123");
            System.out.println("========================================\n");
        };
    }
}
