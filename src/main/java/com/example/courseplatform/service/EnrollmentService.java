package com.example.courseplatform.service;

import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Enrollment;
import com.example.courseplatform.model.User;
import com.example.courseplatform.repository.CourseRepository;
import com.example.courseplatform.repository.EnrollmentRepository;
import com.example.courseplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.courseplatform.dto.EnrollmentResponse;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public EnrollmentResponse enrollUser(String courseId) {
        User user = getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new RuntimeException("You are already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .enrolledAt(LocalDateTime.now())
                .build();

        enrollment = enrollmentRepository.save(enrollment);

        return EnrollmentResponse.builder()
                .enrollmentId(enrollment.getId())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }

    public List<EnrollmentResponse> getUserEnrollments() {
        User user = getCurrentUser();
        return enrollmentRepository.findByUser(user)
                .stream()
                .map(enrollment -> EnrollmentResponse.builder()
                        .enrollmentId(enrollment.getId())
                        .courseId(enrollment.getCourse().getId())
                        .courseTitle(enrollment.getCourse().getTitle())
                        .enrolledAt(enrollment.getEnrolledAt())
                        .build())
                .collect(Collectors.toList());
    }

    public boolean isEnrolled(User user, Course course) {
        return enrollmentRepository.existsByUserAndCourse(user, course);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
