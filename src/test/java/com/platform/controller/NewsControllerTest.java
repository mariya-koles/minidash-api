package com.platform.controller;

import com.platform.dto.NewsDto;
import com.platform.service.NewsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Test
    void getNews_shouldReturnJson() throws Exception {
        NewsDto.Article article = new NewsDto.Article();
        article.setTitle("Sample News");
        article.setDescription("This is a sample news item.");
        article.setAuthor("Test Author");
        article.setUrl("https://example.com/news");
        article.setPublishedAt("2025-05-01T12:00:00Z");

        NewsDto newsResponse = new NewsDto();
        newsResponse.setStatus("ok");
        newsResponse.setTotalResults(1);
        newsResponse.setArticles(List.of(article));

        Mockito.when(newsService.getNews(eq("Java")))
                .thenReturn(newsResponse);

        mockMvc.perform(get("/api/news").param("topic", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.articles[0].title").value("Sample News"))
                .andExpect(jsonPath("$.articles[0].author").value("Test Author"));
    }
}
