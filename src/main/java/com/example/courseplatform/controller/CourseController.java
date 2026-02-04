package com.example.courseplatform.controller;

import com.example.courseplatform.dto.CourseListResponse;
import com.example.courseplatform.dto.EnrollmentResponse;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.service.CourseService;
import com.example.courseplatform.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<CourseListResponse> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<EnrollmentResponse> enrollInCourse(@PathVariable String courseId) {
        return new ResponseEntity<>(enrollmentService.enrollUser(courseId), HttpStatus.CREATED);
    }
}
