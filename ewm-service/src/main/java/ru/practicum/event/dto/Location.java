package ru.practicum.event.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @NotNull(message = "Location lat must be set")
    Double lat;

    @NotNull(message = "Location lon must be set")
    Double lon;
}
