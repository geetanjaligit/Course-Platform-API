package com.example.courseplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompletedSubtopicDTO {
    private String subtopicId;
    private String subtopicTitle;
    private LocalDateTime completedAt;
}
