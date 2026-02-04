package com.example.courseplatform.controller;

import com.example.courseplatform.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.courseplatform.dto.CourseProgressResponse;
import com.example.courseplatform.service.ProgressService;

import com.example.courseplatform.dto.EnrollmentResponse;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final ProgressService progressService;

    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments() {
        return ResponseEntity.ok(enrollmentService.getUserEnrollments());
    }

    @GetMapping("/{enrollmentId}/progress")
    public ResponseEntity<CourseProgressResponse> getEnrollmentProgress(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(progressService.getEnrollmentProgress(enrollmentId));
    }
}
