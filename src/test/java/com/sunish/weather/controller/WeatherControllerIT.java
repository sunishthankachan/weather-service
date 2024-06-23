package com.sunish.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunish.weather.entity.Weather;
import com.sunish.weather.exception.CityNotFoundException;
import com.sunish.weather.exception.InvalidWeatherOperationException;
import com.sunish.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Weather mockWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        Mockito.when(weatherService.getWeather("Auckland")).thenReturn(mockWeather);
        Mockito.when(weatherService.getWeatherOfAllCities()).thenReturn(Collections.singletonList(mockWeather));
    }

    @Test
    void testGetWeather() throws Exception {
        mockMvc.perform(get("/api/v1/weather/Auckland"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Auckland"))
                .andExpect(jsonPath("$.temperature").value("20.5"));
    }

    @Test
    void testGetWeatherNotFound() throws Exception {
        Mockito.when(weatherService.getWeather("UnknownCity")).thenThrow(new CityNotFoundException("City not found: UnknownCity"));

        mockMvc.perform(get("/api/v1/weather/UnknownCity"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("City not found: UnknownCity"));
    }

    @Test
    void testGetWeatherOfAllCities() throws Exception {
        mockMvc.perform(get("/api/v1/weather"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Auckland"))
                .andExpect(jsonPath("$[0].temperature").value("20.5"));
    }

    @Test
    void testAddWeather() throws Exception {
        Weather newWeather = new Weather("Wellington", "15.5", "C", "2024-06-12", "Cloudy");
        Mockito.when(weatherService.addWeather(any(Weather.class))).thenReturn(newWeather);

        mockMvc.perform(post("/api/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWeather)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value("Wellington"))
                .andExpect(jsonPath("$.temperature").value("15.5"));
    }

    @Test
    void testAddWeatherAlreadyExists() throws Exception {
        Weather existingWeather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        Mockito.when(weatherService.addWeather(any(Weather.class))).thenThrow(new InvalidWeatherOperationException("Weather data already exists for city: Auckland"));

        mockMvc.perform(post("/api/v1/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingWeather)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Weather data already exists for city: Auckland"));
    }

    @Test
    void testUpdateWeather() throws Exception {
        Weather updatedWeather = new Weather("Auckland", "25.0", "C", "2024-06-12", "Sunny");
        Mockito.when(weatherService.updateWeather(eq("Auckland"), any(Weather.class))).thenReturn(updatedWeather);

        mockMvc.perform(put("/api/v1/weather/Auckland")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedWeather)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Auckland"))
                .andExpect(jsonPath("$.temperature").value("25.0"));
    }

    @Test
    void testDeleteWeather() throws Exception {
        mockMvc.perform(delete("/api/v1/weather/Auckland"))
                .andExpect(status().isOk());

        verify(weatherService, times(1)).deleteWeather("Auckland");
    }

    @Test
    void testDeleteWeatherNotFound() throws Exception {
        String city = "UnknownCity";
        doThrow(new CityNotFoundException("City not found: " + city)).when(weatherService).deleteWeather(city);

        mockMvc.perform(delete("/api/v1/weather/UnknownCity"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("City not found: UnknownCity"));
    }
}

