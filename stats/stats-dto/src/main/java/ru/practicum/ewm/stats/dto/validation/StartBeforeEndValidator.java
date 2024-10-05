package ru.practicum.ewm.stats.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, StartEndInstantAvailable> {
    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(StartEndInstantAvailable obj, ConstraintValidatorContext constraintValidatorContext) {
        Instant start = obj.getStart();
        Instant end = obj.getEnd();

        return start.isBefore(end);
    }
}
