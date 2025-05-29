package com.platform.service;

import com.platform.config.ExternalApiProperties;
import com.platform.dto.WeatherDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        ExternalApiProperties.Api weatherApi = new ExternalApiProperties.Api();
        weatherApi.setBaseUrl("https://fake-weather.com");

        ExternalApiProperties apiProperties = new ExternalApiProperties();
        apiProperties.setWeather(weatherApi);

        Mockito.when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        Mockito.when(webClientBuilder.build()).thenReturn(webClient);

        weatherService = new WeatherService(apiProperties, webClientBuilder);
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetWeatherForCity_shouldReturnDto() {
        String city = "London";

        // Simulated OpenWeather API response body
        Map<String, Object> apiResponse = Map.of(
                "name", "London",
                "main", Map.of(
                        "temp", 22.5,
                        "feels_like", 23.0,
                        "humidity", 50
                ),
                "wind", Map.of(
                        "speed", 15.0
                ),
                "weather", List.of(Map.of(
                        "description", "clear sky",
                        "icon", "01d"
                ))
        );

        // Mock the WebClient behavior
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .thenReturn(Mono.just(apiResponse));

        // Call the method
        WeatherDto result = weatherService.getWeatherForCity(city);

        // Validate the result
        assertEquals("London", result.getCity());
        assertEquals(22.5, result.getTemperature());
        assertEquals(23.0, result.getFeelsLike());
        assertEquals(50, result.getHumidity());
        assertEquals(15.0, result.getWindSpeed());
        assertEquals("clear sky", result.getCondition());
        assertEquals("01d", result.getIcon());
    }


}