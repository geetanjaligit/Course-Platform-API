package com.example.courseplatform.service;

import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.Topic;
import com.example.courseplatform.model.Subtopic;
import com.example.courseplatform.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeedDataService {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

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
                log.info("Successfully seeded {} courses.", courses.size());
            }

        } catch (Exception e) {
            log.error("Failed to seed data: ", e);
        }
    }
}
