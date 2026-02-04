package com.example.courseplatform.controller;

import com.example.courseplatform.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.courseplatform.dto.SubtopicCompletionResponse;

@RestController
@RequestMapping("/api/subtopics")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/{subtopicId}/complete")
    public ResponseEntity<SubtopicCompletionResponse> markSubtopicComplete(@PathVariable String subtopicId) {
        return ResponseEntity.ok(progressService.markSubtopicComplete(subtopicId));
    }
}
