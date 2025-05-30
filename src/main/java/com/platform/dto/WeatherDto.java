package com.platform.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Weather data returned from the API")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDto {
    private String city;
    private double temperature;
    private double feelsLike;
    private int humidity;
    private double windSpeed;
    private String condition;
    private String icon;
}