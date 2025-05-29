package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.NewsDto;
import com.platform.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NewsService {

    private static final Logger log = LoggerFactory.getLogger(NewsService.class);

    private final ExternalApiProperties externalApiProperties;

    private WebClient webClient;


    public NewsService(ExternalApiProperties externalApiProperties, WebClient.Builder builder) {
        this.externalApiProperties = externalApiProperties;
        this.webClient = builder
                .baseUrl(externalApiProperties.getNews().getBaseUrl())
                .build();
    }

    public NewsDto getNews(String topic) {
        String apiKey = externalApiProperties.getNews().getApiKey();
        NewsDto news = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/everything")
                        .queryParam("q", topic)
                        .queryParam("apiKey", apiKey)
                        .queryParam("language", "en")
                        .build())
                .retrieve()
                .bodyToMono(NewsDto.class)
                .block();
        if (news == null || news.getArticles().isEmpty()) {
            throw new ResourceNotFoundException("No news found for topic: " + topic);
        }
        return news;
    }
}

