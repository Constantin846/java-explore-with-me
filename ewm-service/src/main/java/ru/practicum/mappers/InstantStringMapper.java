package ru.practicum.mappers;

import java.time.Instant;

public interface InstantStringMapper {
    Instant toInstant(String dateTime);

    String toString(Instant instant);
}
