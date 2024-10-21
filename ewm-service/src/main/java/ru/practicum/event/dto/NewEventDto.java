package ru.practicum.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.stats.dto.adapters.InstantDeserializer;
import ru.practicum.ewm.stats.dto.adapters.InstantSerializer;
import ru.practicum.location.dto.LocationDtoRequest;
import ru.practicum.validation.constraints.NullOrNotBlank;
import ru.practicum.validation.groups.CreateValid;
import ru.practicum.validation.groups.UpdateValid;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public class NewEventDto {

    @NotBlank(message = "Event annotation must not be blank", groups = CreateValid.class)
    @NullOrNotBlank(message = "Event annotation must be null or not blank", groups = UpdateValid.class)
    @Length(message = "Event annotation length must be between 20 and 2000 characters inclusive",
            min = 20, max = 2000, groups = {CreateValid.class, UpdateValid.class})
    String annotation;

    @NotNull(message = "Event category id must be set", groups = CreateValid.class)
    Long category;

    @NotBlank(message = "Event description must not be blank", groups = CreateValid.class)
    @NullOrNotBlank(message = "Event description must be null or not blank", groups = UpdateValid.class)
    @Length(message = "Event description length must be between 20 and 7000 characters inclusive",
            min = 20, max = 7000, groups = {CreateValid.class, UpdateValid.class})
    String description;

    @NotNull(message = "Event date must be set", groups = CreateValid.class)
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @FutureOrPresent(message = "Event date must be in the future", groups = {CreateValid.class})
    Instant eventDate;

    @NotNull(message = "Event location must be set", groups = CreateValid.class)
    LocationDtoRequest location;

    Boolean paid = false;

    @PositiveOrZero(message = "Participant limit of event must not be negative",
            groups = {CreateValid.class, UpdateValid.class})
    Integer participantLimit = 0;

    Boolean requestModeration = true;

    @NotBlank(message = "Event title must not be blank", groups = CreateValid.class)
    @NullOrNotBlank(message = "Event title must be null or not blank", groups = UpdateValid.class)
    @Length(message = "Event title length must be between 3 and 120 characters inclusive",
            min = 3, max = 120, groups = {CreateValid.class, UpdateValid.class})
    String title;
}
