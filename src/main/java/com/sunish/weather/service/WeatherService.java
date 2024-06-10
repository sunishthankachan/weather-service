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

/**
 * Service class for managing weather data.
 * It interacts with the WeatherRepository to perform CRUD operations.
 */
@Service
@Slf4j
public class WeatherService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    /**
     * Retrieves weather data for all available cities.
     *
     * @return a list of weather data for all cities.
     * @throws CityListEmptyException if the city list is empty.
     */
    public List<Weather> getWeatherOfAllCities() {
        List<Weather> allWeather = weatherRepository.findAll();
        if (allWeather.isEmpty()) {
            log.error("City list empty");
            throw new CityListEmptyException("City list is empty");
        }
        return allWeather;
    }

    /**
     * Retrieves weather data for a specified city.
     *
     * @param city the name of the city.
     * @return the weather data for the specified city.
     * @throws CityNotFoundException if the city is not found.
     */
    public Weather getWeather(String city) {
        return weatherRepository.findById(city).orElseThrow(() -> {
            log.error("City not found: {}", city);
            return new CityNotFoundException("City not found: " + city);
        });
    }

    /**
     * Adds new weather data for a city.
     *
     * @param newWeather the weather data to be added.
     * @return the added weather data.
     * @throws InvalidWeatherOperationException if the weather data already exists for the city.
     */
    public Weather addWeather(Weather newWeather) {
        if (weatherRepository.existsById(newWeather.getCity())) {
            log.error("Weather data already exists for city: {}", newWeather.getCity());
            throw new InvalidWeatherOperationException("Weather data already exists for city: " + newWeather.getCity());
        }
        weatherRepository.save(newWeather);
        log.info("Weather data added successfully for city: {}", newWeather.getCity());
        return newWeather;
    }

    /**
     * Updates weather data for a specified city.
     *
     * @param city the name of the city.
     * @param updatedWeather the updated weather data.
     * @return the updated weather data.
     * @throws CityNotFoundException if the city is not found.
     */
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

    /**
     * Deletes weather data for a specified city.
     *
     * @param city the name of the city.
     * @throws CityNotFoundException if the city is not found.
     */
    public void deleteWeather(String city) {
        if (!weatherRepository.existsById(city)) {
            log.error("City not found: {}", city);
            throw new CityNotFoundException("City not found: " + city);
        }
        weatherRepository.deleteById(city);
        log.info("Weather data deleted successfully for city: {}", city);
    }
}
