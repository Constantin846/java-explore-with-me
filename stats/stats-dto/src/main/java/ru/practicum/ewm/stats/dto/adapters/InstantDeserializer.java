package ru.practicum.ewm.stats.dto.adapters;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class InstantDeserializer extends JsonDeserializer<Instant> {
    @Value("${ewm-stats-dto.timestamp-pattern}")
    private String pattern;
    private final ZoneId zoneId = ZoneId.of("UTC+0");

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        String dateTime = jsonParser.getValueAsString();
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        return Instant.from(zonedDateTime);
    }
}
