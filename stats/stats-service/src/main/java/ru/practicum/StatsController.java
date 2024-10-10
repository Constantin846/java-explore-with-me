package ru.practicum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoRequest;
import ru.practicum.ewm.stats.dto.StatDtoResponse;
import ru.practicum.ewm.stats.dto.StatsRequestContext;
import ru.practicum.exceptions.JsonSerializeException;
import ru.practicum.service.StatsService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final ObjectMapper objectMapper;
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatDto create(@Valid @RequestBody StatDto statDto) {
        log.info("Create stat: {}", statDto);
        return statsService.create(statDto);
    }

    @GetMapping("/stats")
    public List<StatDtoResponse> findStats(@Valid StatsRequestContext statsRequestContext) {
        StatDtoRequest statDtoRequest = null;
        try {
            String jsObject = objectMapper.writeValueAsString(statsRequestContext);
            statDtoRequest = objectMapper.readValue(jsObject, StatDtoRequest.class);
        } catch (JsonProcessingException e) {
            String message = String
                    .format("Exception during mapping request params to request dto: %s", statsRequestContext);
            log.warn(message);
            throw new JsonSerializeException(message);
        }
        log.info("Find stats by {}", statDtoRequest);
        return statsService.findStats(statDtoRequest);
    }
}
