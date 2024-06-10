package com.sunish.weather.controller;

import com.sunish.weather.entity.Weather;
import com.sunish.weather.exception.CityListEmptyException;
import com.sunish.weather.exception.CityNotFoundException;
import com.sunish.weather.exception.ErrorResponse;
import com.sunish.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/weather")
@Slf4j
@Tag(name = "Weather API", description = "API for weather information")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Operation(summary = "Get weather data for all cities", description = "Fetches weather data for all available cities.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched weather data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "404", description = "City list is empty",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<Map<String, Weather>> getWeather() {
        log.info("Fetching weather data for all cities");
        try {
            Map<String, Weather> allWeather = weatherService.getWeatherOfAllCities();
            log.info("Weather data fetched successfully for all cities");
            return ResponseEntity.ok(allWeather);
        } catch (CityListEmptyException ex) {
            log.error("City list empty");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get weather data for a specific city", description = "Fetches weather data for the specified city.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched weather data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{city}")
    public ResponseEntity<?> getWeather(
            @Parameter(description = "Name of the city to fetch weather data for")
            @PathVariable String city) {
        log.info("Fetching weather data for city: {}", city);
        try {
            Weather weather = weatherService.getWeather(city);
            log.info("Weather data fetched successfully for city: {}", city);
            return ResponseEntity.ok(weather);
        } catch (CityNotFoundException ex) {
            log.error("City not found: {}", city);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("ERR-404", ex.getMessage()));
        }
    }
}
