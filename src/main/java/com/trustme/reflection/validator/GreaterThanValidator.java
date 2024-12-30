package com.trustme.reflection.validator;

import com.trustme.reflection.annotation.GreaterThan;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GreaterThanValidator implements ConstraintValidator<GreaterThan, Double> {
    private Double minValue;

    @Override
    public void initialize(GreaterThan constraintAnnotation) {
        this.minValue = (double) constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value > minValue;
    }

}
