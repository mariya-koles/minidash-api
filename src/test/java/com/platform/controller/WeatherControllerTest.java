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
                .city("Houston")
                .temperature(27.0)
                .feelsLike(26.2)
                .humidity(55)
                .windSpeed(4.1)
                .condition("clear sky")
                .icon("01d")
                .build();

        Mockito.when(weatherService.getWeatherForCity(eq("Houston")))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/weather")
                        .param("city", "Houston"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Houston"))
                .andExpect(jsonPath("$.temperature").value(27.0))
                .andExpect(jsonPath("$.feelsLike").value(26.2))
                .andExpect(jsonPath("$.humidity").value(55))
                .andExpect(jsonPath("$.windSpeed").value(4.1))
                .andExpect(jsonPath("$.condition").value("clear sky"))
                .andExpect(jsonPath("$.icon").value("01d"));
    }
}
