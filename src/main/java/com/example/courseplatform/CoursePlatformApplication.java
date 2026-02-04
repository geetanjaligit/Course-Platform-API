package com.example.courseplatform;

import org.springframework.ai.autoconfigure.transformers.TransformersEmbeddingModelAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { TransformersEmbeddingModelAutoConfiguration.class })
public class CoursePlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoursePlatformApplication.class, args);
    }
}
