package com.sunish.weather.service;

import com.sunish.weather.entity.Weather;
import com.sunish.weather.exception.CityListEmptyException;
import com.sunish.weather.exception.CityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WeatherService {

    private final Map<String, Weather> weatherData = new HashMap<>();

    public Map<String, Weather> getWeatherOfAllCities() {
        if (weatherData.isEmpty()) {
            log.error("City list empty");
            throw new CityListEmptyException("City list is empty");
        }
        return weatherData;
    }

    public Weather getWeather(String city) {
        Weather weather = weatherData.get(city);
        if (weather == null) {
            log.error("City not found: {}", city);
            throw new CityNotFoundException("City not found: " + city);
        }
        return weather;
    }

    public Weather addWeather(String city, Weather newWeather) {
        if (weatherData.containsKey(city)) {
            log.error("Weather data already exists for city: {}", city);
            throw new IllegalArgumentException("Weather data already exists for city: " + city);
        }
        weatherData.put(city, newWeather);
        log.info("Weather data added successfully for city: {}", city);
        return newWeather;
    }
}
