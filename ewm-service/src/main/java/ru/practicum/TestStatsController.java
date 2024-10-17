package ru.practicum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoResponse;
import ru.practicum.ewm.stats.dto.StatsRequestContext;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class TestStatsController { //todo delete
    private final StatsClient statsClient;

    @PostMapping("/hit")
    public StatDto create(@Valid @RequestBody StatDto statDto) {
        log.info("CreateValid stat: {}", statDto);
        return statsClient.create(statDto);
    }

    @GetMapping("/stats")
    public List<StatDtoResponse> findStats(@Valid StatsRequestContext statsRequestContext) {
        log.info("Find stats by {}", statsRequestContext);
        return statsClient.findStats(statsRequestContext);
    }
}
