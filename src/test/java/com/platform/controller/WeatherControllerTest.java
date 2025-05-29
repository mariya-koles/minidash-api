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
                .temperature(25.0)
                .feelsLike(26.3)
                .humidity(50)
                .windSpeed(12.5)
                .condition("Sunny")
                .icon("01d")
                .build();

        Mockito.when(weatherService.getWeatherForCity(eq("London")))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/weather")
                        .param("city", "London"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("London"))
                .andExpect(jsonPath("$.temperature").value(25.0))
                .andExpect(jsonPath("$.feelsLike").value(26.3))
                .andExpect(jsonPath("$.humidity").value(50))
                .andExpect(jsonPath("$.windSpeed").value(12.5))
                .andExpect(jsonPath("$.condition").value("Sunny"))
                .andExpect(jsonPath("$.icon").value("01d"));
    }
}
