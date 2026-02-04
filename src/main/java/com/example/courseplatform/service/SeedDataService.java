package com.example.courseplatform.service;

import com.example.courseplatform.elasticsearch.document.CourseDocument;
import com.example.courseplatform.elasticsearch.repository.CourseSearchRepository;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Topic;
import com.example.courseplatform.model.Subtopic;
import com.example.courseplatform.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class SeedDataService {

    private final CourseRepository courseRepository;
    private final CourseSearchRepository courseSearchRepository;
    private final EmbeddingModel embeddingModel;
    private final ObjectMapper objectMapper;

    public SeedDataService(CourseRepository courseRepository,
            CourseSearchRepository courseSearchRepository,
            Optional<EmbeddingModel> embeddingModel,
            ObjectMapper objectMapper) {
        this.courseRepository = courseRepository;
        this.courseSearchRepository = courseSearchRepository;
        this.embeddingModel = embeddingModel.orElse(null);
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    @Transactional
    public void seedData() {
        if (courseRepository.count() > 0) {
            log.info("Database already contains data, skipping seeding.");
            return;
        }

        try {
            log.info("Seeding data from courses.json...");
            InputStream inputStream = new ClassPathResource("courses.json").getInputStream();
            Map<String, List<Course>> data = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            List<Course> courses = data.get("courses");

            if (courses != null) {
                for (Course course : courses) {
                    // Set parent references for correct JPA mapping
                    if (course.getTopics() != null) {
                        for (Topic topic : course.getTopics()) {
                            topic.setCourse(course);
                            if (topic.getSubtopics() != null) {
                                for (Subtopic subtopic : topic.getSubtopics()) {
                                    subtopic.setTopic(topic);
                                }
                            }
                        }
                    }
                }
                courseRepository.saveAll(courses);
                log.info("Successfully seeded {} courses to DB.", courses.size());
                indexData(courses);
            }

        } catch (Exception e) {
            log.error("Failed to seed data: ", e);
        }
    }

    private void indexData(List<Course> courses) {
        log.info("Indexing data to Elasticsearch...");
        List<CourseDocument> documents = new ArrayList<>();

        for (Course course : courses) {
            // Index Course
            documents.add(CourseDocument.builder()
                    .id(course.getId())
                    .type("COURSE")
                    .title(course.getTitle())
                    .content(course.getDescription())
                    .courseId(course.getId())
                    .courseTitle(course.getTitle())
                    .embedding(getEmbeddingSafe(course.getTitle() + " "
                            + (course.getDescription() != null ? course.getDescription() : "")))
                    .build());

            if (course.getTopics() != null) {
                for (Topic topic : course.getTopics()) {
                    // Index Topic
                    documents.add(CourseDocument.builder()
                            .id(topic.getId())
                            .type("TOPIC")
                            .title(topic.getTitle())
                            .courseId(course.getId())
                            .courseTitle(course.getTitle())
                            .embedding(getEmbeddingSafe(topic.getTitle()))
                            .build());

                    if (topic.getSubtopics() != null) {
                        for (Subtopic subtopic : topic.getSubtopics()) {
                            // Index Subtopic
                            documents.add(CourseDocument.builder()
                                    .id(subtopic.getId())
                                    .type("SUBTOPIC")
                                    .title(subtopic.getTitle())
                                    .content(subtopic.getContent())
                                    .courseId(course.getId())
                                    .courseTitle(course.getTitle())
                                    .topicId(topic.getId())
                                    .topicTitle(topic.getTitle())
                                    .embedding(getEmbeddingSafe(subtopic.getTitle() + " "
                                            + (subtopic.getContent() != null ? subtopic.getContent() : "")))
                                    .build());
                        }
                    }
                }
            }
        }

        courseSearchRepository.saveAll(documents);
        log.info("Successfully indexed {} items to Elasticsearch with embeddings.", documents.size());
    }

    private double[] getEmbeddingSafe(String text) {
        if (embeddingModel == null || text == null) {
            return null;
        }
        try {
            return toDoubleArray(embeddingModel.embed(text));
        } catch (Exception e) {
            log.warn("Failed to generate embedding for text: {}. Error: {}", text, e.getMessage());
            return null;
        }
    }

    private double[] toDoubleArray(List<Double> list) {
        double[] doubleArray = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            doubleArray[i] = list.get(i);
        }
        return doubleArray;
    }
}
