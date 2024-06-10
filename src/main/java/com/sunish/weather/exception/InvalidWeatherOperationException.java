package com.sunish.weather.exception;

/**
 * Custom exception thrown when an invalid operation is performed on weather data.
 * This exception is used to signal conditions where the requested operation on the weather data
 * cannot be completed due to some invalid state or input.
 */
public class InvalidWeatherOperationException extends RuntimeException {

    /**
     * Constructs a new InvalidWeatherOperationException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public InvalidWeatherOperationException(String message) {
        super(message);
    }
}
