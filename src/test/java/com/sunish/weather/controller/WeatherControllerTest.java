package com.sunish.weather.controller;

import com.sunish.weather.WeatherServiceApplication;
import com.sunish.weather.entity.Weather;
import com.sunish.weather.exception.CityNotFoundException;
import com.sunish.weather.exception.CityListEmptyException;
import com.sunish.weather.exception.ErrorResponse;
import com.sunish.weather.exception.InvalidWeatherOperationException;
import com.sunish.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This class defines the unit tests for the WeatherController class.
 * It tests various functionalities of the WeatherController class using Mockito for mocking the WeatherService.
 */
@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }

    /**
     * Test case to verify the behavior of getWeather method in WeatherController class.
     * It verifies that the controller returns correct weather information for a specific city.
     */
    @Test
    @DisplayName("Test Get Weather - Success")
    void testGetWeather() {
        Weather mockWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        when(weatherService.getWeather("Auckland")).thenReturn(mockWeather);

        ResponseEntity<?> responseEntity = weatherController.getWeather("Auckland");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(mockWeather, responseEntity.getBody());

        verify(weatherService, times(1)).getWeather("Auckland");
    }

    /**
     * Test case to verify the behavior of getWeather method in WeatherController class when city is not found.
     * It verifies that the controller returns appropriate error response when city is not found.
     */
    @Test
    @DisplayName("Test Get Weather - City Not Found")
    void testGetWeatherNotFound() {
        String city = "UnknownCity";
        when(weatherService.getWeather(city)).thenThrow(new CityNotFoundException("City not found: " + city));

        ResponseEntity<?> responseEntity = weatherController.getWeather(city);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        verify(weatherService, times(1)).getWeather(city);
    }

    /**
     * Test case to verify the behavior of getWeather method in WeatherController class when city list is empty.
     * It verifies that the controller returns appropriate error response when city list is empty.
     */
    @Test
    void testGetWeatherEmptyCityList() {
        when(weatherService.getWeatherOfAllCities()).thenThrow(new CityListEmptyException("City list is empty"));

        ResponseEntity<?> responseEntity = weatherController.getWeather();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(weatherService, times(1)).getWeatherOfAllCities();
    }

    /**
     * Test case to verify the behavior of addWeather method in WeatherController class.
     * It verifies that the controller successfully adds weather information for a city.
     */
    @Test
    void testAddWeather() {
        Weather mockWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        when(weatherService.addWeather(mockWeather)).thenReturn(mockWeather);

        ResponseEntity<?> responseEntity = weatherController.addWeather(mockWeather);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockWeather, responseEntity.getBody());

        verify(weatherService, times(1)).addWeather(mockWeather);
    }

    /**
     * Test case to verify the behavior of addWeather method in WeatherController class when weather data already exists for a city.
     * It verifies that the controller returns appropriate error response when weather data already exists for a city.
     */
    @Test
    void testAddWeatherAlreadyExists() {
        Weather mockWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        when(weatherService.addWeather(mockWeather)).thenThrow(new InvalidWeatherOperationException("Weather data already exists for city: Auckland"));

        ResponseEntity<?> responseEntity = weatherController.addWeather(mockWeather);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);

        verify(weatherService, times(1)).addWeather(mockWeather);
    }

    /**
     * Test case to verify the behavior of updateWeather method in WeatherController class.
     * It verifies that the controller successfully updates weather information for a city.
     */
    @Test
    void testUpdateWeather() {
        Weather updatedWeather = new Weather("Auckland", "25.0", "C", "2024-06-12", "Sunny");
        when(weatherService.updateWeather("Auckland", updatedWeather)).thenReturn(updatedWeather);

        ResponseEntity<?> responseEntity = weatherController.updateWeather("Auckland", updatedWeather);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedWeather, responseEntity.getBody());

        verify(weatherService, times(1)).updateWeather("Auckland", updatedWeather);
    }

    /**
     * Test case to verify the behavior of deleteWeather method in WeatherController class.
     * It verifies that the controller successfully deletes weather information for a city.
     */
    @Test
    void testDeleteWeather() {
        String city = "Auckland";

        ResponseEntity<?> responseEntity = weatherController.deleteWeather(city);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(weatherService, times(1)).deleteWeather(city);
    }

    /**
     * Test case to verify the behavior of deleteWeather method in WeatherController class when city is not found.
     * It verifies that the controller returns appropriate error response when city is not found.
     */
    @Test
    void testDeleteWeatherNotFound() {
        String city = "UnknownCity";
        // Mocking behavior of getWeather method to throw CityNotFoundException
        doThrow(new CityNotFoundException("City not found: " + city)).when(weatherService).deleteWeather(city);

        ResponseEntity<?> responseEntity = weatherController.deleteWeather(city);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(weatherService, times(1)).deleteWeather(city);
    }
}

