package ru.practicum.location.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDtoRequest {

    Long id;

    String name;

    Long locationType;

    Double lat;

    Double lon;
}
