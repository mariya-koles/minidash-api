package com.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.dto.WeatherDto;
import com.platform.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getWeather_shouldReturnWeatherDto() throws Exception {
        WeatherDto mockResponse = WeatherDto.builder()
                .city("London")
                .description("Sunny")
                .temperature(25.0)
                .icon("01d")
                .build();

        Mockito.when(weatherService.getWeatherForCity(eq("London")))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/weather")
                        .param("city", "London"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("London"))
                .andExpect(jsonPath("$.description").value("Sunny"))
                .andExpect(jsonPath("$.temperature").value(25.0))
                .andExpect(jsonPath("$.icon").value("01d"));
    }
}
