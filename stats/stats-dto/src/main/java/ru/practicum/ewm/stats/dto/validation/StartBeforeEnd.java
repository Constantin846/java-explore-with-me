package ru.practicum.ewm.stats.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEnd {
    String message() default "Start must be before end";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
