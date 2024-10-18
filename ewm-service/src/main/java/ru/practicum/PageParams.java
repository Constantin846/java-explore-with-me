package ru.practicum;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PROTECTED)
public class PageParams {

    @PositiveOrZero(message = "Form param must be positive or zero")
    Integer from = 0;

    @Positive(message = "Size param must be positive")
    Integer size = 10;
}
