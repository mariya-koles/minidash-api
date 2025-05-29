package com.platform.service;

import com.platform.config.ExternalApiProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class NewsService {


    private final ExternalApiProperties externalApiProperties;

    private WebClient webClient;


    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(externalApiProperties.getNews().getBaseUrl())
                .build();
    }

    public Object getNews(String topic) {
        String apiKey = "YOUR_NEWS_API_KEY"; // 🔐 Replace with a real API key
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/everything")
                        .queryParam("q", topic)
                        .queryParam("apiKey", apiKey)
                        .queryParam("language", "en")
                        .build())
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}

