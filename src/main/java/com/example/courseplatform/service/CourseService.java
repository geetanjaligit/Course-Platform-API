package com.example.courseplatform.service;

import com.example.courseplatform.dto.CourseListDTO;
import com.example.courseplatform.dto.CourseListResponse;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseListResponse getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseListDTO> dtoList = courses.stream().map(course -> {
            int subtopicCount = course.getTopics().stream()
                    .mapToInt(topic -> topic.getSubtopics().size())
                    .sum();
            return CourseListDTO.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .description(course.getDescription())
                    .topicCount(course.getTopics().size())
                    .subtopicCount(subtopicCount)
                    .build();
        }).collect(Collectors.toList());

        return CourseListResponse.builder().courses(dtoList).build();
    }

    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
}
