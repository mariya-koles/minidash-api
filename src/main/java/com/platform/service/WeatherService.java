package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.WeatherDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private static Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final ExternalApiProperties externalApiProperties;

    private WebClient webClient;

    public WeatherService(ExternalApiProperties externalApiProperties, WebClient.Builder builder) {
        this.externalApiProperties = externalApiProperties;
        this.webClient = builder
                .baseUrl(externalApiProperties.getWeather().getBaseUrl())
                .build();
    }

    public WeatherDto getWeatherForCity(String city) {
        String apiKey = externalApiProperties.getWeather().getApiKey();
        String baseUrl = externalApiProperties.getWeather().getBaseUrl();
        // Encode city name for safe URL use

        String weatherRequestUrl = baseUrl +
                "?q=" + URLEncoder.encode(city, StandardCharsets.UTF_8) +
                "&units=metric" +
                "&appid=" + apiKey;

        Map<String, Object> response = webClient.get()
                .uri(weatherRequestUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        Map<String, Object> main = (Map<String, Object>) response.get("main");
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");

        WeatherDto dto = new WeatherDto();
        dto.setCity((String) response.get("name"));
        dto.setTemperature(((Number) main.get("temp")).doubleValue());
        dto.setFeelsLike(((Number) main.get("feels_like")).doubleValue());
        dto.setHumidity(((Number) main.get("humidity")).intValue());
        dto.setWindSpeed(((Number) wind.get("speed")).doubleValue());

        if (weatherList != null && !weatherList.isEmpty()) {
            dto.setCondition((String) weatherList.get(0).get("description"));
            dto.setIcon((String) weatherList.get(0).get("icon"));
        }

        return dto;
    }
}
