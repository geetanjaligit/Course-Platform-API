package com.example.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseListDTO {
    private String id;
    private String title;
    private String description;
    private int topicCount;
    private int subtopicCount;
}
