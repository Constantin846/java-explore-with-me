package ru.practicum.event.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @NotNull(message = "Location lat must be set")
    @Max(value = 90)
    @Min(value = -90)
    Double lat;

    @NotNull(message = "Location lon must be set")
    @Max(value = 180)
    @Min(value = -180)
    Double lon;
}
