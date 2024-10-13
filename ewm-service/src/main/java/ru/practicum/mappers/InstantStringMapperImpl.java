package ru.practicum.mappers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class InstantStringMapperImpl implements InstantStringMapper {
    @Value("${ewm-stats-dto.timestamp-pattern}")
    private String pattern;
    private final ZoneId zoneId = ZoneId.of("UTC+0");

    public Instant toInstant(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        return Instant.from(zonedDateTime);
    }

    public String toString(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        return zonedDateTime.format(formatter);
    }
}
