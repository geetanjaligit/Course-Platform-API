package com.example.courseplatform.service;

import com.example.courseplatform.dto.SearchResultDTO;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Subtopic;
import com.example.courseplatform.model.Topic;
import com.example.courseplatform.repository.CourseRepository;
import com.example.courseplatform.repository.SubtopicRepository;
import com.example.courseplatform.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;

    public List<SearchResultDTO> search(String query) {
        List<SearchResultDTO> results = new ArrayList<>();

        // Search Courses
        List<Course> courses = courseRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query,
                query);
        results.addAll(courses.stream().map(c -> SearchResultDTO.builder()
                .id(c.getId())
                .type(SearchResultDTO.ResultType.COURSE)
                .title(c.getTitle())
                .snippet(c.getDescription() != null && c.getDescription().length() > 100
                        ? c.getDescription().substring(0, 100) + "..."
                        : c.getDescription())
                .courseId(c.getId())
                .build()).collect(Collectors.toList()));

        // Search Topics
        List<Topic> topics = topicRepository.findByTitleContainingIgnoreCase(query);
        results.addAll(topics.stream().map(t -> SearchResultDTO.builder()
                .id(t.getId())
                .type(SearchResultDTO.ResultType.TOPIC)
                .title(t.getTitle())
                .courseId(t.getCourse().getId())
                .build()).collect(Collectors.toList()));

        // Search Subtopics
        List<Subtopic> subtopics = subtopicRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
        results.addAll(subtopics.stream().map(s -> SearchResultDTO.builder()
                .id(s.getId())
                .type(SearchResultDTO.ResultType.SUBTOPIC)
                .title(s.getTitle())
                .snippet(s.getContent() != null && s.getContent().length() > 100
                        ? s.getContent().substring(0, 100) + "..."
                        : s.getContent())
                .courseId(s.getTopic().getCourse().getId())
                .topicId(s.getTopic().getId())
                .build()).collect(Collectors.toList()));

        return results;
    }
}
