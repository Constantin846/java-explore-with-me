package ru.practicum.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.stats.dto.adapters.InstantDeserializer;
import ru.practicum.ewm.stats.dto.adapters.InstantSerializer;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {

    @NotBlank(message = "Event annotation must not be blank")
    @Length(message = "Event annotation length must be between 20 and 2000 characters inclusive", min = 20, max = 2000)
    String annotation;

    Long category;

    @NotBlank(message = "Event description must not be blank")
    @Length(message = "Event description length must be between 20 and 7000 characters inclusive", min = 20, max = 7000)
    String description;

    @NotNull(message = "Event date must be set")
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @FutureOrPresent(message = "Event date must be in the future")
    Instant eventDate;

    @NotNull(message = "Event location must be set")
    Location location;

    boolean paid = false;

    int participantLimit = 0;

    boolean requestModeration = false;

    @NotBlank(message = "Event title must not be blank")
    @Length(message = "Event title length must be between 3 and 120 characters inclusive", min = 3, max = 120)
    String title;
}
