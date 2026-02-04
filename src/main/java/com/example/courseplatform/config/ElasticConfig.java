package com.example.courseplatform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;

@Configuration
public class ElasticConfig extends ElasticsearchConfiguration {

    @Value("${ELASTICSEARCH_URL}")
    private String elasticUrl;

    @Value("${ELASTICSEARCH_API_KEY}")
    private String apiKey;

    @Override
    public ClientConfiguration clientConfiguration() {
        // Clean the URL: Remove protocols (https://, http://) and trailing slashes
        String cleanedUrl = elasticUrl
                .replace("https://", "")
                .replace("http://", "");

        if (cleanedUrl.endsWith("/")) {
            cleanedUrl = cleanedUrl.substring(0, cleanedUrl.length() - 1);
        }

        return ClientConfiguration.builder()
                .connectedTo(cleanedUrl)
                .usingSsl()
                .withHeaders(() -> {
                    HttpHeaders headers = new HttpHeaders();
                    // Elastic Cloud requires 'ApiKey ' prefix
                    headers.add("Authorization", "ApiKey " + apiKey);
                    return headers;
                })
                .build();
    }
}
