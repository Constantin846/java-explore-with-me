package ru.practicum.location.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDto {

    Long id;

    String name;

    LocationTypeDto locationType;

    Double lat;

    Double lon;
}
