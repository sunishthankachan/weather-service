package com.sunish.weather.exception;

/**
 * Custom exception thrown when a city is not found.
 * This exception extends the {@link RuntimeException}.
 */
public class CityNotFoundException extends RuntimeException {

    /**
     * Constructs a new CityNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public CityNotFoundException(String message) {
        super(message);
    }
}
