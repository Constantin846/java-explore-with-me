package ru.practicum.request.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.stats.dto.adapters.InstantSerializer;
import ru.practicum.request.model.Status;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {

    Long id;

    @JsonSerialize(using = InstantSerializer.class)
    Instant created;

    Long event;

    Long requester;

    Status status;
}
