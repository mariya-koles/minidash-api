package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.WeatherDto;
import com.platform.exception.ResourceNotFoundException;
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
        String geoUrl = externalApiProperties.getWeather().getAdditionalUrl();
        String baseUrl = externalApiProperties.getWeather().getBaseUrl();
        // Encode city name for safe URL use
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String geoRequestUrl = geoUrl + "?q=" + encodedCity + "&limit=1&appid=" + apiKey;

        //Geocode the city name → lat/lon
        List<Map<String, Object>> geoResults = webClient.get()
                .uri(geoRequestUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();

        if (geoResults == null || geoResults.isEmpty()) {
            throw new ResourceNotFoundException("City not found: " + city);
        }

        double lat = ((Number) geoResults.get(0).get("lat")).doubleValue();
        double lon = ((Number) geoResults.get(0).get("lon")).doubleValue();

        // Call One Call 3.0 API using lat/lon
        String weatherRequestUrl = baseUrl +
                "?lat=" + lat +
                "&lon=" + lon +
                "&exclude=minutely,hourly,daily,alerts" +
                "&units=metric" +
                "&appid=" + apiKey;

        Map<String, Object> oneCallData = webClient.get()
                .uri(weatherRequestUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (oneCallData == null || !oneCallData.containsKey("current")) {
            throw new ResourceNotFoundException("No weather data found for city: " + city);
        }

        Map<String, Object> current = (Map<String, Object>) oneCallData.get("current");

        //Map to WeatherDto
        WeatherDto dto = new WeatherDto();
        dto.setCity(city);
        dto.setTemperature(((Number) current.get("temp")).doubleValue());
        dto.setFeelsLike(((Number) current.get("feels_like")).doubleValue());
        dto.setHumidity(((Number) current.get("humidity")).intValue());
        dto.setWindSpeed(((Number) current.get("wind_speed")).doubleValue());

        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) current.get("weather");
        if (weatherList != null && !weatherList.isEmpty()) {
            Map<String, Object> weather = weatherList.get(0);
            dto.setCondition((String) weather.get("description"));
            dto.setIcon((String) weather.get("icon"));
        }

        return dto;
    }
}
