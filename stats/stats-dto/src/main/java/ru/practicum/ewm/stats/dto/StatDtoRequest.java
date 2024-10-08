package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.stats.dto.adapters.InstantDeserializer;
import ru.practicum.ewm.stats.dto.adapters.InstantSerializer;
import ru.practicum.ewm.stats.dto.validation.StartBeforeEnd;
import ru.practicum.ewm.stats.dto.validation.StartEndInstantAvailable;

import java.time.Instant;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@StartBeforeEnd(message = "Start of stat request must be before end")
public class StatDtoRequest implements StartEndInstantAvailable {

    @NotNull(message = "Start period of needed stats must be set")
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @Past(message = "Start time must be in the past")
    Instant start;

    @NotNull(message = "End period of needed stats must be set")
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    //@PastOrPresent(message = "End time must not be in the future")
    Instant end;

    //@NotEmpty(message = "Uris of needed stats must be set")
    List<String> uris;

    boolean unique = false;
}
