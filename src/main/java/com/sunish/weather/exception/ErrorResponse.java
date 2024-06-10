package com.sunish.weather.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErrorResponse is a simple POJO used to represent error responses.
 * This class includes an error code and an error message.
 * Lombok annotations are used to generate boilerplate code like getters, setters, and constructors.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String errorCode; // error code representing the specific error
    private String errorMessage; // detailed error message.

}
