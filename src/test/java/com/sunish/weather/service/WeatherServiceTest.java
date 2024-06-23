package com.sunish.weather.service;

import com.sunish.weather.WeatherServiceApplication;
import com.sunish.weather.entity.Weather;
import com.sunish.weather.exception.CityListEmptyException;
import com.sunish.weather.exception.CityNotFoundException;
import com.sunish.weather.exception.InvalidWeatherOperationException;
import com.sunish.weather.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * This class defines the unit tests for the WeatherService class.
 * It tests various functionalities of the WeatherService class using Mockito for mocking the WeatherRepository.
 */
@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = WeatherServiceApplication.class)
class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherService weatherService;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }

    /**
     * Test case to verify the behavior of getWeatherOfAllCities method in WeatherService class.
     * It verifies that the method returns correct weather information for all cities.
     */
    @Test
    void testGetWeatherOfAllCities() {
        Weather mockWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        List<Weather> mockWeatherList = Collections.singletonList(mockWeather);

        when(weatherRepository.findAll()).thenReturn(mockWeatherList);

        List<Weather> result = weatherService.getWeatherOfAllCities();

        assertEquals(1, result.size());
        assertEquals(mockWeather, result.get(0));

        verify(weatherRepository, times(1)).findAll();
    }

    /**
     * Test case to verify the behavior of getWeatherOfAllCities method in WeatherService class when city list is empty.
     * It verifies that the method throws CityListEmptyException when city list is empty.
     */
    @Test
    void testGetWeatherOfAllCitiesEmpty() {
        when(weatherRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(CityListEmptyException.class, () -> weatherService.getWeatherOfAllCities());

        verify(weatherRepository, times(1)).findAll();
    }

    /**
     * Test case to verify the behavior of getWeather method in WeatherService class.
     * It verifies that the method returns correct weather information for a specific city.
     */
    @Test
    void testGetWeather() {
        Weather mockWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");

        when(weatherRepository.findById(any())).thenReturn(Optional.of(mockWeather));

        Weather result = weatherService.getWeather("Auckland");

        assertEquals(mockWeather, result);

        verify(weatherRepository, times(1)).findById("Auckland");
    }

    /**
     * Test case to verify the behavior of getWeather method in WeatherService class when city is not found.
     * It verifies that the method throws CityNotFoundException when city is not found.
     */
    @Test
    void testGetWeatherNotFound() {
        when(weatherRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> weatherService.getWeather("UnknownCity"));

        verify(weatherRepository, times(1)).findById("UnknownCity");
    }

    /**
     * Test case to verify the behavior of addWeather method in WeatherService class.
     * It verifies that the method successfully adds weather information for a city.
     */
    @Test
    void testAddWeather() {
        Weather newWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        when(weatherRepository.existsById("Auckland")).thenReturn(false);

        Weather savedWeather = weatherService.addWeather(newWeather);

        assertNotNull(savedWeather);
        assertEquals(newWeather, savedWeather);

        verify(weatherRepository, times(1)).existsById("Auckland");
        verify(weatherRepository, times(1)).save(newWeather);
    }

    /**
     * Parameterized test for the addWeather method in WeatherService class.
     * This test verifies the behavior of adding weather data for a city in two scenarios:
     * - When the city does not already exist in the repository (successful addition).
     * - When the city already exists in the repository (throws InvalidWeatherOperationException).
     *
     * @param city        the name of the city
     * @param temperature the temperature value
     * @param unit        the unit of the temperature
     * @param date        the date of the weather data
     * @param description the weather description
     * @param exists      whether the city already exists in the repository
     * @param shouldThrow whether the test should expect an InvalidWeatherOperationException
     */
    @ParameterizedTest
    @CsvSource({
            "Auckland, 20.5, C, 2024-06-12, Sunny, false, false",
            "Auckland, 20.5, C, 2024-06-12, Sunny, true, true"
    })
    void testAddWeather(String city, String temperature, String unit, String date, String description, boolean exists, boolean shouldThrow) {
        Weather newWeather = new Weather(city, temperature, unit, date, description);
        when(weatherRepository.existsById(city)).thenReturn(exists);

        if (shouldThrow) {
            assertThrows(InvalidWeatherOperationException.class, () -> weatherService.addWeather(newWeather));
            verify(weatherRepository, times(1)).existsById(city);
            verify(weatherRepository, never()).save(newWeather);
        } else {
            Weather savedWeather = weatherService.addWeather(newWeather);
            assertNotNull(savedWeather);
            assertEquals(newWeather, savedWeather);
            verify(weatherRepository, times(1)).existsById(city);
            verify(weatherRepository, times(1)).save(newWeather);
        }
    }

    /**
     * Test case to verify the behavior of addWeather method in WeatherService class when weather data already exists for a city.
     * It verifies that the method throws InvalidWeatherOperationException when weather data already exists for a city.
     */
    @Test
    void testAddWeatherAlreadyExists() {
        Weather existingWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        when(weatherRepository.existsById("Auckland")).thenReturn(true);

        assertThrows(InvalidWeatherOperationException.class, () -> weatherService.addWeather(existingWeather));

        verify(weatherRepository, times(1)).existsById("Auckland");
        verify(weatherRepository, never()).save(existingWeather);
    }

    /**
     * Test case to verify the behavior of updateWeather method in WeatherService class.
     * It verifies that the method successfully updates weather information for a city.
     */
    @Test
    void testUpdateWeather() {
        String city = "Auckland";
        Weather existingWeather = new Weather(city, "20.5", "C", "2024-06-12", "Sunny");
        Weather updatedWeather = new Weather(city, "25.0", "C", "2024-06-13", "Cloudy");
        when(weatherRepository.findById(city)).thenReturn(Optional.of(existingWeather));

        Weather result = weatherService.updateWeather(city, updatedWeather);

        assertNotNull(result);
        assertEquals(updatedWeather, result);

        verify(weatherRepository, times(1)).findById(city);
        verify(weatherRepository, times(1)).save(updatedWeather);
    }

    /**
     * Parameterized test for the updateWeather method in WeatherService class.
     * This test verifies the behavior of updating weather data for a city in two scenarios:
     * - When the city is found in the repository (successful update).
     * - When the city is not found in the repository (throws CityNotFoundException).
     *
     * @param city           the name of the city
     * @param temperature    the new temperature value
     * @param unit           the new unit of the temperature
     * @param date           the new date of the weather data
     * @param description    the new weather description
     * @param cityFound      whether the city is found in the repository
     * @param shouldThrow    whether the test should expect a CityNotFoundException
     */
    @ParameterizedTest
    @CsvSource({
            "Auckland, 25.0, C, 2024-06-13, Cloudy, true, false",
            "UnknownCity, 25.0, C, 2024-06-13, Cloudy, false, true"
    })
    void testUpdateWeather(String city, String temperature, String unit, String date, String description, boolean cityFound, boolean shouldThrow) {
        Weather existingWeather = new Weather(city, "20.5", "C", "2024-06-12", "Sunny");
        Weather updatedWeather = new Weather(city, temperature, unit, date, description);
        when(weatherRepository.findById(city)).thenReturn(cityFound ? Optional.of(existingWeather) : Optional.empty());

        if (shouldThrow) {
            assertThrows(CityNotFoundException.class, () -> weatherService.updateWeather(city, updatedWeather));
            verify(weatherRepository, times(1)).findById(city);
            verify(weatherRepository, never()).save(updatedWeather);
        } else {
            Weather result = weatherService.updateWeather(city, updatedWeather);
            assertNotNull(result);
            assertEquals(updatedWeather, result);
            verify(weatherRepository, times(1)).findById(city);
            verify(weatherRepository, times(1)).save(updatedWeather);
        }
    }

    /**
     * Test case to verify the behavior of updateWeather method in WeatherService class when city is not found.
     * It verifies that the method throws CityNotFoundException when city is not found.
     */
    @Test
    void testUpdateWeatherNotFound() {
        String city = "UnknownCity";
        Weather updatedWeather = new Weather(city, "25.0", "C", "2024-06-13", "Cloudy");
        when(weatherRepository.findById(city)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> weatherService.updateWeather(city, updatedWeather));

        verify(weatherRepository, times(1)).findById(city);
        verify(weatherRepository, never()).save(updatedWeather);
    }

    /**
     * Test case to verify the behavior of deleteWeather method in WeatherService class.
     * It verifies that the method successfully deletes weather information for an existing city.
     */
    @Test
    void testDeleteWeather() {
        String city = "Auckland";
        Weather existingWeather = new Weather(city, "20.5", "C", "2024-06-12", "Sunny");
        when(weatherRepository.existsById(city)).thenReturn(true);

        weatherService.deleteWeather(city);

        verify(weatherRepository, times(1)).existsById(city);
        verify(weatherRepository, times(1)).deleteById(city);
    }

    /**
     * Parameterized test for the deleteWeather method in WeatherService class.
     * This test verifies the behavior of deleting weather data for a city in two scenarios:
     * - When the city is found in the repository (successful deletion).
     * - When the city is not found in the repository (throws CityNotFoundException).
     *
     * @param city         the name of the city
     * @param cityFound    whether the city is found in the repository
     * @param shouldThrow  whether the test should expect a CityNotFoundException
     */
    @ParameterizedTest
    @CsvSource({
            "Auckland, true, false",
            "UnknownCity, false, true"
    })
    void testDeleteWeather(String city, boolean cityFound, boolean shouldThrow) {
        when(weatherRepository.existsById(city)).thenReturn(cityFound);

        if (shouldThrow) {
            assertThrows(CityNotFoundException.class, () -> weatherService.deleteWeather(city));
            verify(weatherRepository, times(1)).existsById(city);
            verify(weatherRepository, never()).deleteById(city);
        } else {
            weatherService.deleteWeather(city);
            verify(weatherRepository, times(1)).existsById(city);
            verify(weatherRepository, times(1)).deleteById(city);
        }
    }

    /**
     * Test case to verify the behavior of deleteWeather method in WeatherService class when city is not found.
     * It verifies that the method throws CityNotFoundException when city is not found.
     */
    @Test
    void testDeleteWeatherNotFound() {
        String city = "UnknownCity";
        when(weatherRepository.existsById(city)).thenReturn(false);

        assertThrows(CityNotFoundException.class, () -> weatherService.deleteWeather(city));

        verify(weatherRepository, times(1)).existsById(city);
        verify(weatherRepository, never()).deleteById(city);
    }
}
