package com.trustme.reflection.validator;

import com.trustme.reflection.annotation.NullToEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullToEmptyValidator implements ConstraintValidator<NullToEmpty, String> {
    @Override
    public void initialize(NullToEmpty constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
    public String process(String value) {
        return value == null ? "" : value;
    }
}
