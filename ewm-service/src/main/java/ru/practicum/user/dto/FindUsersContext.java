package ru.practicum.user.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FindUsersContext {

    List<Long> ids;

    @PositiveOrZero(message = "Form param must be positive or zero")
    Integer from = 0;

    @Positive(message = "Size param must be positive")
    Integer size = 10;

}
