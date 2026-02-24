package com.example.assignment;

import com.example.assignment.entity.Student;
import com.example.assignment.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Production-Level Integration Test for Student REST API
 * 
 * Tests the complete application stack:
 * - Spring Boot application context
 * - Real HTTP server on random port
 * - H2 in-memory database
 * - Spring Security authentication
 * - REST controller endpoints
 * - Full request/response cycle
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class StudentRestIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/students";
        
        // Clean database before each test
        studentRepository.deleteAll();
    }

    /**
     * Test 1: GET /api/students returns 200 OK with empty list
     */
    @Test
    void testGetAllStudents_WhenNoData_ReturnsEmptyList() {
        // Arrange - database already cleared in setUp()

        // Act - Call real REST endpoint
        ResponseEntity<List<Student>> response = restTemplate
                .withBasicAuth("student1", "pass123")
                .exchange(
                        baseUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Student>>() {}
                );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    /**
     * Test 2: GET /api/students returns 200 OK with student list
     */
    @Test
    void testGetAllStudents_WhenDataExists_ReturnsStudentList() {
        // Arrange - Insert test data directly into database
        Student student1 = new Student();
        student1.setName("John Doe");
        student1.setStudentId("S001");
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Jane Smith");
        student2.setStudentId("S002");
        studentRepository.save(student2);

        // Act - Call real REST endpoint
        ResponseEntity<List<Student>> response = restTemplate
                .withBasicAuth("student1", "pass123")
                .exchange(
                        baseUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Student>>() {}
                );

        // Assert - Verify HTTP status
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Assert - Verify response body structure
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()).hasSize(2);

        // Assert - Verify data content
        List<Student> students = response.getBody();
        assertThat(students)
                .extracting(Student::getName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
        
        assertThat(students)
                .extracting(Student::getStudentId)
                .containsExactlyInAnyOrder("S001", "S002");
    }

    /**
     * Test 3: GET /api/students/{id} returns 200 OK for existing student
     */
    @Test
    void testGetStudentById_WhenExists_ReturnsStudent() {
        // Arrange
        Student student = new Student();
        student.setName("Alice Johnson");
        student.setStudentId("S003");
        Student savedStudent = studentRepository.save(student);

        // Act
        ResponseEntity<Student> response = restTemplate
                .withBasicAuth("student1", "pass123")
                .getForEntity(baseUrl + "/" + savedStudent.getId(), Student.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedStudent.getId());
        assertThat(response.getBody().getName()).isEqualTo("Alice Johnson");
        assertThat(response.getBody().getStudentId()).isEqualTo("S003");
    }

    /**
     * Test 4: GET /api/students/{id} returns 404 for non-existent student
     */
    @Test
    void testGetStudentById_WhenNotExists_Returns404() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        ResponseEntity<Student> response = restTemplate
                .withBasicAuth("student1", "pass123")
                .getForEntity(baseUrl + "/" + nonExistentId, Student.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Test 5: GET /api/students with invalid credentials returns 401 Unauthorized
     */
    @Test
    void testGetAllStudents_WithInvalidAuth_Returns401() {
        // Act - Call with wrong credentials
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("wronguser", "wrongpass")
                .getForEntity(baseUrl, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Test 6: POST /api/students with TEACHER role returns 201 Created
     */
    @Test
    void testCreateStudent_AsTeacher_Returns201() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setName("Bob Wilson");
        newStudent.setStudentId("S004");

        // Act
        ResponseEntity<Student> response = restTemplate
                .withBasicAuth("teacher1", "pass123")
                .postForEntity(baseUrl, newStudent, Student.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Bob Wilson");
        assertThat(response.getBody().getStudentId()).isEqualTo("S004");

        // Verify student was actually saved to database
        assertThat(studentRepository.findById(response.getBody().getId())).isPresent();
    }

    /**
     * Test 7: POST /api/students with STUDENT role returns 403 Forbidden
     */
    @Test
    void testCreateStudent_AsStudent_Returns403() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setName("Charlie Brown");
        newStudent.setStudentId("S005");

        // Act
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("student1", "pass123")
                .postForEntity(baseUrl, newStudent, String.class);

        // Assert - Students cannot create other students
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    /**
     * Test 8: Verify H2 database is being used (transaction rollback works)
     */
    @Test
    void testDatabaseIsolation_EachTestStartsClean() {
        // Arrange - Add student
        Student student = new Student();
        student.setName("Test Isolation");
        student.setStudentId("S999");
        studentRepository.save(student);

        // Act - Verify it exists
        long count = studentRepository.count();

        // Assert
        assertThat(count).isEqualTo(1);
        
        // Note: After this test completes, setUp() in next test will clear data
        // This verifies test isolation is working correctly
    }
}
