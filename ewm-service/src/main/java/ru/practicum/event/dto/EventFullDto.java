package ru.practicum.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.State;
import ru.practicum.ewm.stats.dto.adapters.InstantSerializer;
import ru.practicum.location.dto.LocationDto;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto extends EventShortDto {

    @JsonSerialize(using = InstantSerializer.class)
    Instant createdOn;

    String description;

    LocationDto location;

    Integer participantLimit;

    @JsonSerialize(using = InstantSerializer.class)
    Instant publishedOn;

    Boolean requestModeration;

    State state;

    String title;
}
