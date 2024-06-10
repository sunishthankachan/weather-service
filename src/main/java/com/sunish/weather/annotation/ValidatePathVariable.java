package com.sunish.weather.annotation;

import com.sunish.weather.validator.CityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating path variables to ensure they meet specific constraints.
 * This annotation can be applied to method parameters and fields.
 *
 * The validation logic is implemented in the {@link CityValidator} class.
 */
@Constraint(validatedBy = CityValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePathVariable {

    /**
     * Error message to be returned if the path variable is invalid.
     *
     * @return the error message
     */
    String message() default "Invalid path variable";

    /**
     * Allows the specification of validation groups, to which this constraint belongs.
     *
     * @return the array of classes representing the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Jakarta Bean Validation API to assign custom
     * payload objects to a constraint.
     *
     * @return the array of classes representing the payload
     */
    Class<? extends Payload>[] payload() default {};
}
