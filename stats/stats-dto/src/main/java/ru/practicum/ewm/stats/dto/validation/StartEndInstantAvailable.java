package ru.practicum.ewm.stats.dto.validation;

import java.time.Instant;

public interface StartEndInstantAvailable {
    Instant getStart();

    Instant getEnd();
}
