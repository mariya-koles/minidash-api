package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.WeatherDto;
import com.platform.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        WeatherDto weather = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherDto.class)
                .block();
        if (weather == null) {
            throw new ResourceNotFoundException("No weather found for city: " + city);
        }
        return weather;
    }
}
