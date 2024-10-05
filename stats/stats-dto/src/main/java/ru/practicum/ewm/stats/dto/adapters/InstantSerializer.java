package ru.practicum.ewm.stats.dto.adapters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class InstantSerializer extends JsonSerializer<Instant>{
    @Value("${ewm-stats-dto.timestamp-pattern}")
    private String pattern;
    private final ZoneId zoneId = ZoneId.of("UTC+0");

    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        String dateTime = zonedDateTime.format(formatter);
        jsonGenerator.writeString(dateTime);
    }
}
