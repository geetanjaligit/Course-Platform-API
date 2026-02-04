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
public class CourseSearchResult {
    private String courseId;
    private String courseTitle;
    private List<SearchMatch> matches;
}
