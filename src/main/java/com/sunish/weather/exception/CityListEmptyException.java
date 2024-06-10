package com.sunish.weather.exception;

public class CityListEmptyException extends RuntimeException {

    public CityListEmptyException(String message) {
        super(message);
    }
}
