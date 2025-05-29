package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.NewsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NewsServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private NewsService newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ExternalApiProperties.Api newsApi = new ExternalApiProperties.Api();
        newsApi.setBaseUrl("https://fake-newsapi.org/v2");
        newsApi.setApiKey("fake-key");

        ExternalApiProperties props = new ExternalApiProperties();
        props.setNews(newsApi);

        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        newsService = new NewsService(props, webClientBuilder);
    }

    @Test
    void getNews_shouldReturnNewsResponse() {
        String topic = "Java";

        NewsDto.Article article = new NewsDto.Article();
        article.setTitle("Java 21 Released");
        article.setAuthor("OpenJDK Team");
        article.setUrl("https://example.com/java-21");
        article.setPublishedAt("2025-05-29T12:00:00Z");

        NewsDto expectedResponse = new NewsDto();
        expectedResponse.setStatus("ok");
        expectedResponse.setTotalResults(1);
        expectedResponse.setArticles(List.of(article));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(NewsDto.class)).thenReturn(Mono.just(expectedResponse));

        NewsDto actualResponse = newsService.getNews(topic);
        assertEquals(expectedResponse, actualResponse);
    }

}
