package ru.practicum.event.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventState {

    @NotNull(message = "State action must be set")
    StateAction stateAction;
} // todo delete
