package com.platform.controller;

import com.platform.dto.NewsDto;
import com.platform.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private static Logger log = LoggerFactory.getLogger(NewsController.class);
    private final NewsService newsService;


    @GetMapping
    @Operation(
            summary = "Get news articles for a topic",
            responses = {
                    @ApiResponse(responseCode = "200", description = "News retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Missing or invalid topic parameter"),
                    @ApiResponse(responseCode = "500", description = "Error retrieving news data")
            }
    )
    public ResponseEntity<NewsDto> getNews(@RequestParam String topic) {
        NewsDto news = newsService.getNews(topic);
        return ResponseEntity.ok(news);
    }
}
