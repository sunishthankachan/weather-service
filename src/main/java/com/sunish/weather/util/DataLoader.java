package com.sunish.weather.util;

import com.sunish.weather.entity.Weather;
import com.sunish.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final WeatherService weatherService;

    @Autowired
    public DataLoader(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Loading initial weather data...");

        Weather city1Weather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        Weather city2Weather = new Weather("Wellington", "25.8", "C", "2024-06-12", "Cloudy");
        Weather city3Weather = new Weather("Christchurch", "18.2", "C", "2024-06-12", "Rainy");

        weatherService.addWeather("Auckland", city1Weather);
        weatherService.addWeather("Wellington", city2Weather);
        weatherService.addWeather("Christchurch", city3Weather);

        log.info("Initial weather data loaded successfully.");
    }
}