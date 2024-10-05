package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.stats.dto.adapters.InstantDeserializer;
import ru.practicum.ewm.stats.dto.adapters.InstantSerializer;
import ru.practicum.ewm.stats.dto.validation.StartBeforeEnd;
import ru.practicum.ewm.stats.dto.validation.StartEndInstantAvailable;

import java.time.Instant;
import java.util.Collection;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@StartBeforeEnd(message = "Start of stat request must be before end")
public class StatDtoRequest implements StartEndInstantAvailable {

    @NotNull(message = "Start period of needed stats must be set")
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @Past
    Instant start;

    @NotNull(message = "End period of needed stats must be set")
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @PastOrPresent
    Instant end;

    @NotEmpty(message = "Uris of needed stats must be set")
    Collection<String> uris;

    boolean unique = false;
}
