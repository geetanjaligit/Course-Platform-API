package com.example.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseProgressResponse {
    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private long totalSubtopics;
    private long completedSubtopics;
    private double completionPercentage;
    private List<CompletedSubtopicDTO> completedItems;
}
