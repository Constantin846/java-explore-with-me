package ru.practicum.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.ewm.stats.dto.adapters.InstantSerializer;
import ru.practicum.user.dto.UserShortDto;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PROTECTED)
public class EventShortDto {

    Long id;

    String annotation;

    CategoryDto category;

    Integer confirmedRequests;

    @JsonSerialize(using = InstantSerializer.class)
    //@JsonDeserialize(using = InstantDeserializer.class) //todo
    Instant eventDate;

    UserShortDto initiator;

    Boolean paid;

    String title;

    Long views;
}