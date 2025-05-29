package com.platform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NewsService {

    private final WebClient webClient;

    public NewsService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://newsapi.org/v2")
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

