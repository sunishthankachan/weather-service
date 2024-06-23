package com.sunish.weather.service;

import com.sunish.weather.entity.Weather;
import com.sunish.weather.exception.CityListEmptyException;
import com.sunish.weather.exception.CityNotFoundException;
import com.sunish.weather.exception.InvalidWeatherOperationException;
import com.sunish.weather.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherServiceIT {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherRepository weatherRepository;

    @BeforeEach
    void setUp() {
        weatherRepository.deleteAll();
        Weather weather1 = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        Weather weather2 = new Weather("Wellington", "18.0", "C", "2024-06-12", "Cloudy");
        weatherRepository.save(weather1);
        weatherRepository.save(weather2);
    }

    @Test
    void testGetWeatherOfAllCities() {
        List<Weather> weatherList = weatherService.getWeatherOfAllCities();
        assertEquals(2, weatherList.size());
    }

    @Test
    void testGetWeatherOfAllCitiesEmpty() {
        weatherRepository.deleteAll();
        assertThrows(CityListEmptyException.class, () -> weatherService.getWeatherOfAllCities());
    }

    @Test
    void testGetWeather() {
        Weather weather = weatherService.getWeather("Auckland");
        assertNotNull(weather);
        assertEquals("Auckland", weather.getCity());
        assertEquals("20.5", weather.getTemperature());
    }

    @Test
    void testGetWeatherNotFound() {
        assertThrows(CityNotFoundException.class, () -> weatherService.getWeather("UnknownCity"));
    }

    @Test
    void testAddWeather() {
        Weather newWeather = new Weather("Christchurch", "10.0", "C", "2024-06-12", "Rainy");
        Weather savedWeather = weatherService.addWeather(newWeather);
        assertNotNull(savedWeather);
        assertEquals("Christchurch", savedWeather.getCity());
        assertEquals("10.0", savedWeather.getTemperature());
    }

    @Test
    void testAddWeatherAlreadyExists() {
        Weather existingWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        assertThrows(InvalidWeatherOperationException.class, () -> weatherService.addWeather(existingWeather));
    }

    @Test
    void testUpdateWeather() {
        Weather updatedWeather = new Weather("Auckland", "25.0", "C", "2024-06-13", "Cloudy");
        Weather result = weatherService.updateWeather("Auckland", updatedWeather);
        assertNotNull(result);
        assertEquals("25.0", result.getTemperature());
        assertEquals("Cloudy", result.getWeather());
    }

    @Test
    void testUpdateWeatherNotFound() {
        Weather updatedWeather = new Weather("UnknownCity", "25.0", "C", "2024-06-13", "Cloudy");
        assertThrows(CityNotFoundException.class, () -> weatherService.updateWeather("UnknownCity", updatedWeather));
    }

    @Test
    void testDeleteWeather() {
        weatherService.deleteWeather("Auckland");
        Optional<Weather> deletedWeather = weatherRepository.findById("Auckland");
        assertTrue(deletedWeather.isEmpty());
    }

    @Test
    void testDeleteWeatherNotFound() {
        assertThrows(CityNotFoundException.class, () -> weatherService.deleteWeather("UnknownCity"));
    }
}
