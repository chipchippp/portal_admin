package com.portal.identity_service.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.*;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {
    private int minAge;

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        minAge = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (Objects.isNull(dob)) {
            return true; // Consider null as valid, use @NotNull for null checks
        }
        long year = ChronoUnit.YEARS.between(dob, LocalDate.now());
        return year >= minAge;
    }
}
