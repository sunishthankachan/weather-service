package com.sunish.weather.exception;

/**
 * Custom exception thrown when the list of cities is empty.
 * This exception extends the {@link RuntimeException}.
 */
public class CityListEmptyException extends RuntimeException {

    /**
     * Constructs a new CityListEmptyException with the specified detail message.
     *
     * @param message the detail message
     */
    public CityListEmptyException(String message) {
        super(message);
    }
}
