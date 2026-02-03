package com.example.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDTO {
    public enum ResultType {
        COURSE, TOPIC, SUBTOPIC
    }

    private String id;
    private ResultType type;
    private String title;
    private String snippet;
    private String courseId;
    private String topicId;
}
