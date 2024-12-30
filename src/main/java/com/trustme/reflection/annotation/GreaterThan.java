package com.trustme.reflection.annotation;

import com.trustme.reflection.validator.GreaterThanValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = GreaterThanValidator.class)
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GreaterThan {
    String message() default "The value must be greater than {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value();
}
