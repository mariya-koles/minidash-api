package com.platform.service;

import com.platform.dto.WeatherDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.platform.config.ExternalApiProperties;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
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

        // Mock response
        Map<String, Object> mockResponse = Map.of(
                "name", city,
                "main", Map.of("temp", 22.5),
                "weather", List.of(Map.of("description", "clear sky", "icon", "01d"))
        );

        // Setup mock chain
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockResponse));

        WeatherDto result = weatherService.getWeatherForCity(city);

        assertEquals("London", result.getCity());
        assertEquals("clear sky", result.getDescription());
        assertEquals(22.5, result.getTemperature());
        assertEquals("01d", result.getIcon());
    }
}