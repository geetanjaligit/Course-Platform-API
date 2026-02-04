package com.example.courseplatform.elasticsearch.repository;

import com.example.courseplatform.elasticsearch.document.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSearchRepository extends ElasticsearchRepository<CourseDocument, String> {
    List<CourseDocument> findByTitleContainingOrContentContaining(String title, String content);
}
