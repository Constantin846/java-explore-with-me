package ru.practicum.event.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.validation.groups.CreateValid;
import ru.practicum.validation.groups.UpdateValid;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventRequest extends NewEventDto {

    Boolean paid;

    @PositiveOrZero(message = "Participant limit of event must not be negative",
            groups = {CreateValid.class, UpdateValid.class})
    Integer participantLimit;

    Boolean requestModeration;

    StateAction stateAction;
}
