package com.example.courseplatform.elasticsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "course_content", createIndex = false)
public class CourseDocument {
    @Id
    private String id; // this will be the subtopicId or topicId or courseId

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    @Field(type = FieldType.Keyword)
    private String type; // "COURSE", "TOPIC", "SUBTOPIC"

    @Field(type = FieldType.Keyword)
    private String courseId;

    @Field(type = FieldType.Keyword)
    private String courseTitle;

    @Field(type = FieldType.Keyword)
    private String topicId;

    @Field(type = FieldType.Keyword)
    private String topicTitle;

    @Field(type = FieldType.Double, index = false)
    private double[] embedding;
}
