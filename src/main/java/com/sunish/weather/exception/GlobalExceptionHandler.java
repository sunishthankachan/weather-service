package com.sunish.weather.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler handles exceptions that occur throughout the application.
 * It uses Spring's @RestControllerAdvice to apply globally to all controllers.
 * This class logs the exceptions and returns appropriate HTTP responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles CityListEmptyException.
     *
     * @param ex the exception thrown when the city list is empty.
     * @return a ResponseEntity with a 404 NOT FOUND status and the exception message.
     */
    @ExceptionHandler(CityListEmptyException.class)
    public ResponseEntity<String> handleCityNotFoundException(CityListEmptyException ex) {
        log.error("City list empty: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles CityNotFoundException.
     *
     * @param ex the exception thrown when a city is not found.
     * @return a ResponseEntity with a 404 NOT FOUND status and the exception message.
     */
    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<String> handleCityNotFoundException(CityNotFoundException ex) {
        log.error("City not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles ConstraintViolationException.
     *
     * @param ex the exception thrown when a constraint violation occurs.
     * @return a ResponseEntity with a 400 BAD REQUEST status and a generic error message.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleException(ConstraintViolationException ex) {
        log.error("Constraint violation error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Input Data");
    }

    /**
     * Handles all other exceptions.
     *
     * @param ex the exception thrown.
     * @return a ResponseEntity with a 500 INTERNAL SERVER ERROR status and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
