package com.example.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMatch {
    private String type;
    private String topicTitle;
    private String subtopicId;
    private String subtopicTitle;
    private String snippet;
}
