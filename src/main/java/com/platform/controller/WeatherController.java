package com.platform.controller;

import com.platform.dto.WeatherDto;
import com.platform.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;


    @GetMapping
    @Operation(
            summary = "Get current weather for a city",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Weather retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Missing or invalid city parameter"),
                    @ApiResponse(responseCode = "500", description = "Error retrieving weather data")
            }
    )
    public ResponseEntity<WeatherDto> getWeather(@RequestParam String city) {
        WeatherDto weather= weatherService.getWeatherForCity(city);
        return ResponseEntity.ok(weather);
    }
}

