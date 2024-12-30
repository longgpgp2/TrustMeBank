package com.trustme.reflection.annotation;


import com.trustme.reflection.validator.NullToEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NullToEmptyValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullToEmpty {
    String message() default "Value was null and converted to empty string";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
