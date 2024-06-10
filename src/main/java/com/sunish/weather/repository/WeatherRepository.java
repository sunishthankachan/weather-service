package com.sunish.weather.repository;

import com.sunish.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Weather entities.
 * This interface extends JpaRepository, which provides basic CRUD operations for the Weather entity.
 * The entity type is Weather, and the identifier type is String (representing the city).
 */
public interface WeatherRepository extends JpaRepository<Weather, String> {
}
