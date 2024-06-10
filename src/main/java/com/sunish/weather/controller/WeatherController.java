package com.sunish.weather.controller;

import com.sunish.weather.annotation.ValidatePathVariable;
import com.sunish.weather.entity.Weather;
import com.sunish.weather.exception.CityListEmptyException;
import com.sunish.weather.exception.CityNotFoundException;
import com.sunish.weather.exception.ErrorResponse;
import com.sunish.weather.exception.InvalidWeatherOperationException;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * REST controller for managing weather data.
 */
@RestController
@RequestMapping("/api/v1/weather")
@Validated
@Slf4j
@Tag(name = "Weather API", description = "API for weather information")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Fetches weather data for all available cities.
     *
     * @return ResponseEntity containing a list of weather data or a 404 status if no cities are available.
     */
    @Operation(summary = "Get weather data for all cities", description = "Fetches weather data for all available cities.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched weather data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "404", description = "City list is empty",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Weather>> getWeather() {
        log.info("Fetching weather data for all cities");
        try {
            List<Weather> allWeather = weatherService.getWeatherOfAllCities();
            log.info("Weather data fetched successfully for all cities");
            return ResponseEntity.ok(allWeather);
        } catch (CityListEmptyException ex) {
            log.error("City list empty");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Fetches weather data for a specified city.
     *
     * @param city the name of the city
     * @return ResponseEntity containing the weather data or a 404 status if the city is not found.
     */
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
            @PathVariable @ValidatePathVariable String city) {
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

    /**
     * Adds weather data for a specified city.
     *
     * @param weather the weather data to be added
     * @return ResponseEntity containing the added weather data or a 400 status if the city already exists.
     */
    @Operation(summary = "Add weather data for a city", description = "Adds weather data for a specified city.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added weather data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> addWeather(
            @Parameter(description = "Weather data to be added", required = true)
            @RequestBody Weather weather) {
        log.info("Adding weather data for city: {}", weather.getCity());
        try {
            Weather addedWeather = weatherService.addWeather(weather);
            log.info("Weather data added successfully for city: {}", weather.getCity());
            return ResponseEntity.status(HttpStatus.CREATED).body(addedWeather);
        } catch (InvalidWeatherOperationException ex) {
            log.error("City already present: {}", weather.getCity());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("ERR-400", ex.getMessage()));
        }
    }

    /**
     * Updates weather data for a specified city.
     *
     * @param city the name of the city
     * @param updatedWeather the updated weather data
     * @return ResponseEntity containing the updated weather data or a 404 status if the city is not found.
     */
    @Operation(summary = "Update weather data for a specific city", description = "Updates weather data for the specified city.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated weather data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{city}")
    public ResponseEntity<?> updateWeather(
            @Parameter(description = "Name of the city to update weather data for")
            @PathVariable String city, @RequestBody Weather updatedWeather) {
        log.info("Updating weather data for city: {}", city);
        try {
            Weather weather = weatherService.updateWeather(city, updatedWeather);
            log.info("Weather data updated successfully for city: {}", city);
            return ResponseEntity.ok(weather);
        } catch (CityNotFoundException ex) {
            log.error("City not found: {}", city);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("ERR-404", ex.getMessage()));
        }
    }

    /**
     * Deletes weather data for a specified city.
     *
     * @param city the name of the city
     * @return ResponseEntity containing a 200 status if the weather data was successfully deleted or a 404 status if the city is not found.
     */
    @Operation(summary = "Delete weather data for a specific city", description = "Deletes weather data for the specified city.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted weather data"),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{city}")
    public ResponseEntity<?> deleteWeather(
            @Parameter(description = "Name of the city to delete weather data for")
            @PathVariable String city) {
        log.info("Deleting weather data for city: {}", city);
        try {
            weatherService.deleteWeather(city);
            log.info("Weather data deleted successfully for city: {}", city);
            return ResponseEntity.ok().build();
        } catch (CityNotFoundException ex) {
            log.error("City not found: {}", city);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("ERR-404", ex.getMessage()));
        }
    }
}
