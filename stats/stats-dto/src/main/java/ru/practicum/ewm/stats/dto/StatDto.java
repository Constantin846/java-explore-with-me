package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.stats.dto.adapters.InstantDeserializer;
import ru.practicum.ewm.stats.dto.adapters.InstantSerializer;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatDto {

    Long id;

    @NotBlank(message = "App field must not be blank")
    String app;

    @NotBlank(message = "Uri field must not be blank")
    String uri;

    @NotBlank(message = "Ip field must not be blank")
    String ip;

    @NotNull(message = "Time must be set")
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @PastOrPresent(message = "Timestamp must not be future")
    Instant timestamp;
}
