package com.sunish.weather.exception;

public class InvalidWeatherOperationException extends RuntimeException {

    public InvalidWeatherOperationException(String message) {
        super(message);
    }
}
