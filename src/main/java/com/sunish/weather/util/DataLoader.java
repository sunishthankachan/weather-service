package com.sunish.weather.util;

import com.sunish.weather.entity.Weather;
import com.sunish.weather.repository.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * DataLoader is a component that initializes the database with some predefined weather data.
 * This class implements ApplicationRunner, so it will run automatically when the Spring Boot application starts.
 * It is used to load initial weather data for Auckland, Wellington, and Christchurch into the H2 database.
 */
@Component
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final WeatherRepository weatherRepository;

    @Autowired
    public DataLoader(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Loading initial weather data...");

        Weather city1Weather = new Weather("Auckland", "20.5", "C", "2024-06-12", "Sunny");
        Weather city2Weather = new Weather("Wellington", "25.8", "C", "2024-06-12", "Cloudy");
        Weather city3Weather = new Weather("Christchurch", "18.2", "C", "2024-06-12", "Rainy");

        weatherRepository.save(city1Weather);
        weatherRepository.save(city2Weather);
        weatherRepository.save(city3Weather);

        log.info("Initial weather data loaded successfully.");
    }
}