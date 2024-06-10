package com.sunish.weather.validator;

import com.sunish.weather.annotation.ValidatePathVariable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * CityValidator is a custom validator class used to validate city names in path variables.
 * It implements the ConstraintValidator interface.
 */
public class CityValidator implements ConstraintValidator<ValidatePathVariable, String> {

    // Regular expression pattern to validate city names
    private static final Pattern VALID_PATH_VARIABLE_PATTERN = Pattern.compile("[a-zA-Z0-9-]+");

    /**
     * Validates the city name based on the defined pattern.
     *
     * @param value   The city name to be validated
     * @param context The constraint validator context
     * @return true if the city name is valid, false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Check if the value is not null and matches the pattern
        return value != null && VALID_PATH_VARIABLE_PATTERN.matcher(value).matches();
    }
}
