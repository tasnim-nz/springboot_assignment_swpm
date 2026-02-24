# Spring Boot Assignment - Test Documentation

## 📋 Overview

Comprehensive test suite for the Spring Boot Assignment application with:
- **Unit Tests**: Service layer testing with Mockito
- **Integration Tests**: Controller layer testing with MockMvc
- **H2 In-Memory Database**: For test isolation
- **CI/CD Pipeline**: GitHub Actions workflow

---

## 🧪 Test Structure

```
src/test/java/com/example/assignment/
├── service/
│   ├── StudentServiceTest.java        # Unit tests for StudentService
│   ├── TeacherServiceTest.java        # Unit tests for TeacherService
│   └── CourseServiceTest.java         # Unit tests for CourseService
└── controller/
    ├── StudentControllerIntegrationTest.java   # Integration tests for Student API
    ├── TeacherControllerIntegrationTest.java   # Integration tests for Teacher API
    └── CourseControllerIntegrationTest.java    # Integration tests for Course API

src/test/resources/
└── application-test.properties        # H2 database configuration
```

---

## 🔧 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Docker** (optional, for containerized testing)

---

## 🚀 Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Only Unit Tests
```bash
mvn test -Dtest=**/*ServiceTest
```

### Run Only Integration Tests
```bash
mvn test -Dtest=**/*IntegrationTest
```

### Run Specific Test Class
```bash
mvn test -Dtest=StudentServiceTest
```

### Run with Coverage Report
```bash
mvn clean verify
mvn jacoco:report
# Open target/site/jacoco/index.html in browser
```

---

## 📊 Test Coverage

### Unit Tests

#### **StudentServiceTest**
- ✅ Create student
- ✅ Register from UI
- ✅ Find all students
- ✅ Find student by ID (success)
- ✅ Find student by ID (not found)
- ✅ Remove student
- ✅ Find by student name
- ✅ Check existence (true/false)

#### **TeacherServiceTest**
- ✅ Create teacher
- ✅ Register from UI
- ✅ Find all teachers
- ✅ Find teacher by ID (success)
- ✅ Find teacher by ID (not found)
- ✅ Delete teacher
- ✅ Check existence (true/false)

#### **CourseServiceTest**
- ✅ Create course
- ✅ Add course from UI
- ✅ Find all courses
- ✅ Find course by ID (success)
- ✅ Find course by ID (not found)
- ✅ Delete course
- ✅ Assign student to course
- ✅ Check existence (true/false)

### Integration Tests

#### **StudentControllerIntegrationTest**
- ✅ POST /api/students (TEACHER - authorized)
- ✅ POST /api/students (STUDENT - forbidden)
- ✅ POST /api/students (unauthorized)
- ✅ GET /api/students (success)
- ✅ GET /api/students/{id} (success)
- ✅ GET /api/students/{id} (not found)
- ✅ DELETE /api/students/{id} (TEACHER - success)
- ✅ DELETE /api/students/{id} (STUDENT - forbidden)
- ✅ PUT /api/students/{id} (TEACHER - success)
- ✅ PUT /api/students/{id} (STUDENT - forbidden)

#### **TeacherControllerIntegrationTest**
- ✅ POST /api/teachers (TEACHER - authorized)
- ✅ POST /api/teachers (STUDENT - forbidden)
- ✅ GET /api/teachers (success)
- ✅ GET /api/teachers/{id} (success)
- ✅ DELETE /api/teachers/{id} (TEACHER - success)
- ✅ DELETE /api/teachers/{id} (STUDENT - forbidden)
- ✅ PUT /api/teachers/{id} (TEACHER - success)

#### **CourseControllerIntegrationTest**
- ✅ POST /api/courses (TEACHER - authorized)
- ✅ POST /api/courses (STUDENT - forbidden)
- ✅ POST /api/courses (unauthorized)
- ✅ GET /api/courses (success)
- ✅ GET /api/courses/{id} (success)
- ✅ GET /api/courses/{id} (not found)
- ✅ DELETE /api/courses/{id} (TEACHER - success)
- ✅ DELETE /api/courses/{id} (STUDENT - forbidden)
- ✅ PUT /api/courses/{id} (TEACHER - success)
- ✅ PUT /api/courses/{id} (STUDENT - forbidden)

---

## 🔐 Security Testing

### Role-Based Access Control (RBAC)

| Operation | STUDENT | TEACHER |
|-----------|---------|---------|
| View Students/Teachers/Courses | ✅ | ✅ |
| Create Student | ❌ 403 | ✅ |
| Update Student | ❌ 403 | ✅ |
| Delete Student | ❌ 403 | ✅ |
| Create Teacher | ❌ 403 | ✅ |
| Delete Teacher | ❌ 403 | ✅ |
| Create Course | ❌ 403 | ✅ |
| Delete Course | ❌ 403 | ✅ |

---

## 🐳 Docker Testing

### Build and Test with Docker

```bash
# Build Docker image
docker build -t assignment-app .

# Run tests in Docker
docker run --rm assignment-app mvn test

# Run with docker-compose
docker-compose up --build
```

---

## 🤖 CI/CD Pipeline (GitHub Actions)

The project includes a complete CI/CD pipeline that:

1. **Triggers on**:
   - Push to `main` or `master` branch
   - Pull requests to `main` or `master`

2. **Pipeline Steps**:
   - ✅ Checkout code
   - ✅ Setup Java 17 (Temurin)
   - ✅ Cache Maven dependencies
   - ✅ Run `mvn clean verify`
   - ✅ Run unit tests
   - ✅ Run integration tests
   - ✅ Generate coverage report
   - ✅ Build Docker image
   - ✅ Test Docker container
   - ❌ Fail build if tests fail

### View CI/CD Results

```bash
# Check workflow status
https://github.com/YOUR_USERNAME/YOUR_REPO/actions
```

---

## 📝 Test Configuration

### application-test.properties

```properties
# H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Auto-create and drop schema
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Random port for parallel test execution
server.port=0
```

---

## 🛠️ Troubleshooting

### Tests Failing

```bash
# Clean and rebuild
mvn clean install -DskipTests
mvn test

# Check for port conflicts
netstat -ano | findstr :8081
```

### H2 Database Issues

```bash
# Enable H2 console for debugging
spring.h2.console.enabled=true
# Access: http://localhost:8081/h2-console
```

### Security Test Failures

Ensure `@WithMockUser` annotations are present:
```java
@WithMockUser(username = "teacher1", roles = {"TEACHER"})
```

---

## 📈 Best Practices Implemented

1. **Test Isolation**: Each test uses `@Transactional` to rollback changes
2. **Mock Objects**: Services are mocked using `@Mock` and `@InjectMocks`
3. **Arrange-Act-Assert**: Tests follow AAA pattern
4. **Descriptive Names**: Test methods clearly describe what they test
5. **Verification**: All repository calls are verified with `verify()`
6. **H2 Database**: Fast in-memory database for testing
7. **Security Context**: Tests use `@WithMockUser` for authentication

---

## 📚 Additional Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Spring Security Testing](https://docs.spring.io/spring-security/reference/servlet/test/index.html)

---

## ✅ Test Execution Checklist

- [ ] All unit tests pass
- [ ] All integration tests pass
- [ ] No security vulnerabilities
- [ ] Code coverage > 80%
- [ ] Docker build succeeds
- [ ] CI/CD pipeline passes
- [ ] No test data leakage
- [ ] Tests run in isolation

---

## 📞 Support

For issues or questions:
1. Check test logs in `target/surefire-reports/`
2. Review GitHub Actions logs
3. Check application-test.properties configuration

---

**Happy Testing! 🎉**
