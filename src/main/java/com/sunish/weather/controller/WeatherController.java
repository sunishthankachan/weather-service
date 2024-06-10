package com.sunish.weather.controller;

import com.sunish.weather.entity.Weather;
import com.sunish.weather.exception.CityListEmptyException;
import com.sunish.weather.exception.CityNotFoundException;
import com.sunish.weather.exception.ErrorResponse;
import com.sunish.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

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

    @GetMapping("/{city}")
    public ResponseEntity<?> getWeather(@PathVariable String city) {
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

