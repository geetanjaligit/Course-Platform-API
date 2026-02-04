package com.example.courseplatform.service;

import com.example.courseplatform.model.*;
import com.example.courseplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import com.example.courseplatform.dto.CourseProgressResponse;
import com.example.courseplatform.dto.SubtopicCompletionResponse;
import com.example.courseplatform.dto.CompletedSubtopicDTO;

@Service
@RequiredArgsConstructor
public class ProgressService {

        private final SubtopicProgressRepository subtopicProgressRepository;
        private final SubtopicRepository subtopicRepository;
        private final UserRepository userRepository;
        private final EnrollmentRepository enrollmentRepository;

        @Transactional
        public SubtopicCompletionResponse markSubtopicComplete(String subtopicId) {
                User user = getCurrentUser();
                Subtopic subtopic = subtopicRepository.findById(subtopicId)
                                .orElseThrow(() -> new RuntimeException("Subtopic not found"));

                Course course = subtopic.getTopic().getCourse();
                if (!enrollmentRepository.existsByUserAndCourse(user, course)) {
                        throw new RuntimeException("User must be enrolled in the course to track progress");
                }

                SubtopicProgress progress = subtopicProgressRepository.findByUserAndSubtopic(user, subtopic)
                                .orElse(SubtopicProgress.builder()
                                                .user(user)
                                                .subtopic(subtopic)
                                                .build());

                progress.setCompleted(true);
                progress.setCompletedAt(LocalDateTime.now());

                progress = subtopicProgressRepository.save(progress);

                return SubtopicCompletionResponse.builder()
                                .subtopicId(progress.getSubtopic().getId())
                                .completed(progress.isCompleted())
                                .completedAt(progress.getCompletedAt())
                                .build();
        }

        public CourseProgressResponse getEnrollmentProgress(Long enrollmentId) {
                User user = getCurrentUser();
                Enrollment enrollment = enrollmentRepository.findByIdAndUser(enrollmentId, user)
                                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

                Course course = enrollment.getCourse();
                String courseId = course.getId();

                long totalSubtopics = subtopicRepository.countByTopic_Course_Id(courseId);
                List<SubtopicProgress> completedProgress = subtopicProgressRepository
                                .findByUserAndSubtopic_Topic_Course_IdAndCompleted(user, courseId, true);

                long completedCount = completedProgress.size();
                double percentage = totalSubtopics > 0 ? (double) completedCount / totalSubtopics * 100 : 0;

                List<CompletedSubtopicDTO> completedItems = completedProgress.stream()
                                .map(p -> CompletedSubtopicDTO.builder()
                                                .subtopicId(p.getSubtopic().getId())
                                                .subtopicTitle(p.getSubtopic().getTitle())
                                                .completedAt(p.getCompletedAt())
                                                .build())
                                .toList();

                return CourseProgressResponse.builder()
                                .enrollmentId(enrollment.getId())
                                .courseId(courseId)
                                .courseTitle(course.getTitle())
                                .totalSubtopics(totalSubtopics)
                                .completedSubtopics(completedCount)
                                .completionPercentage(Math.round(percentage * 100.0) / 100.0)
                                .completedItems(completedItems)
                                .build();
        }

        private User getCurrentUser() {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));
        }
}
