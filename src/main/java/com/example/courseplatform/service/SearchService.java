package com.example.courseplatform.service;

import com.example.courseplatform.dto.*;
import com.example.courseplatform.elasticsearch.document.CourseDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService {

        private final ElasticsearchOperations elasticsearchOperations;

        public SearchResponse search(String query) {
                Map<String, CourseSearchResult> courseMap = new HashMap<>();

                //Build a robust search query using Multi-Match with Fuzziness
                // This handles typos and multi-word phrases correctly.
                NativeQuery searchKeywords = NativeQuery.builder()
                                .withQuery(q -> q.multiMatch(m -> m
                                                .fields("title", "content")
                                                .query(query)
                                                .fuzziness("AUTO")))
                                .build();

                SearchHits<CourseDocument> hits = elasticsearchOperations.search(searchKeywords, CourseDocument.class);

                //Process Hits
                for (SearchHit<CourseDocument> hit : hits) {
                        CourseDocument doc = hit.getContent();
                        CourseSearchResult csr = courseMap.computeIfAbsent(doc.getCourseId(),
                                        id -> CourseSearchResult.builder()
                                                        .courseId(doc.getCourseId())
                                                        .courseTitle(doc.getCourseTitle())
                                                        .matches(new ArrayList<>())
                                                        .build());

                        String type = doc.getType().toLowerCase();
                        if (type.equals("subtopic") && !doc.getTitle().toLowerCase().contains(query.toLowerCase())) {
                                type = "content";
                        }

                        csr.getMatches().add(SearchMatch.builder()
                                        .type(type)
                                        .topicTitle(doc.getTopicTitle())
                                        .subtopicId(doc.getType().equals("SUBTOPIC") ? doc.getId() : null)
                                        .subtopicTitle(doc.getType().equals("SUBTOPIC") ? doc.getTitle() : null)
                                        .snippet(truncate(doc.getContent()))
                                        .build());
                }

                return SearchResponse.builder()
                                .query(query)
                                .results(new ArrayList<>(courseMap.values()))
                                .build();
        }

        private String truncate(String text) {
                if (text == null)
                        return null;
                return text.length() > 100 ? text.substring(0, 100) + "..." : text;
        }
}
