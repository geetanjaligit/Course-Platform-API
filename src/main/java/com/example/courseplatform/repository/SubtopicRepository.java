package com.example.courseplatform.repository;

import com.example.courseplatform.model.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtopicRepository extends JpaRepository<Subtopic, String> {
    List<Subtopic> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}
