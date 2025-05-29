package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.WeatherDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class WeatherService {

    private final ExternalApiProperties externalApiProperties;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(externalApiProperties.getWeather().getBaseUrl())
                .build();
    }

    public WeatherDto getWeatherForCity(String city) {
        String apiKey = System.getenv("OPENWEATHER_API_KEY");

        Map<String, Object> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String cityName = (String) response.get("name");

        Map<String, Object> main = (Map<String, Object>) response.get("main");
        double temp = main != null ? ((Number) main.get("temp")).doubleValue() : 0.0;

        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
        Map<String, Object> weather = (weatherList != null && !weatherList.isEmpty()) ? weatherList.get(0) : Map.of();

        String description = (String) weather.get("description");
        String icon = (String) weather.get("icon");

        return WeatherDto.builder()
                .city(cityName)
                .description(description)
                .temperature(temp)
                .icon(icon)
                .build();
    }
}
