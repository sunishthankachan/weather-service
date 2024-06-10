package com.sunish.weather.service;

import com.sunish.weather.entity.Weather;
import com.sunish.weather.exception.CityListEmptyException;
import com.sunish.weather.exception.CityNotFoundException;
import com.sunish.weather.exception.InvalidWeatherOperationException;
import com.sunish.weather.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WeatherService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public List<Weather> getWeatherOfAllCities() {
        List<Weather> allWeather = weatherRepository.findAll();
        if (allWeather.isEmpty()) {
            log.error("City list empty");
            throw new CityListEmptyException("City list is empty");
        }
        return allWeather;
    }

    public Weather getWeather(String city) {
        return weatherRepository.findById(city).orElseThrow(() -> {
            log.error("City not found: {}", city);
            return new CityNotFoundException("City not found: " + city);
        });
    }

    public Weather addWeather(Weather newWeather) {
        if (weatherRepository.existsById(newWeather.getCity())) {
            log.error("Weather data already exists for city: {}", newWeather.getCity());
            throw new InvalidWeatherOperationException("Weather data already exists for city: " + newWeather.getCity());
        }
        weatherRepository.save(newWeather);
        log.info("Weather data added successfully for city: {}", newWeather.getCity());
        return newWeather;
    }

    public Weather updateWeather(String city, Weather updatedWeather) {
        Weather existingWeather = weatherRepository.findById(city).orElseThrow(() -> {
            log.error("City not found: {}", city);
            return new CityNotFoundException("City not found: " + city);
        });
        updatedWeather.setCity(existingWeather.getCity());
        weatherRepository.save(updatedWeather);
        log.info("Weather data updated successfully for city: {}", city);
        return updatedWeather;
    }

    public void deleteWeather(String city) {
        if (!weatherRepository.existsById(city)) {
            log.error("City not found: {}", city);
            throw new CityNotFoundException("City not found: " + city);
        }
        weatherRepository.deleteById(city);
        log.info("Weather data deleted successfully for city: {}", city);
    }
}
