package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoRequest;
import ru.practicum.ewm.stats.dto.StatDtoResponse;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestStatsController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    public StatDto create(@Validated @RequestBody StatDto statDto) {
        log.info("Create stat: {}", statDto);
        return statsClient.create(statDto);
    }

    @GetMapping("/stats")
    public List<StatDtoResponse> findStats(@Validated @RequestBody StatDtoRequest statDtoRequest) {
        log.info("Find stats by {}", statDtoRequest);
        return statsClient.findStats(statDtoRequest);
    }
}
