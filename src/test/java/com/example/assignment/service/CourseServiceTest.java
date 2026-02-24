package com.example.assignment.service;

import com.example.assignment.entity.Course;
import com.example.assignment.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course1;

    @BeforeEach
    void setUp() {
        course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Software Engineering");
        course1.setCourseCode("SE101");
    }

    @Test
    void findAllCourses() {
        // Arrange
        when(courseRepository.findAll())
                .thenReturn(Arrays.asList(course1));

        // Act
        List<Course> courses = courseService.findAllCourses();

        // Assert
        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals("Software Engineering", courses.get(0).getTitle());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        // Arrange
        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course1));

        // Act
        Optional<Course> foundCourse = courseService.findById(1L);

        // Assert
        assertTrue(foundCourse.isPresent());
        assertEquals("SE101", foundCourse.get().getCourseCode());

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void saveCourse() {
        when(courseRepository.save(any(Course.class)))
                .thenReturn(course1);

        Course saved = courseService.saveCourse(course1);

        assertNotNull(saved);
        verify(courseRepository, times(1)).save(course1);
    }

    @Test
    void deleteCourse() {
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }
}