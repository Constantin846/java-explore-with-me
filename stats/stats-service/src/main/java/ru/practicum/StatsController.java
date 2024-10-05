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
import ru.practicum.service.StatsService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public StatDto create(@Validated @RequestBody StatDto statDto) {
        log.info("Create stat: {}", statDto);
        return statsService.create(statDto);
    }

    @GetMapping("/stats")
    public List<StatDtoResponse> findStats(@Validated @RequestBody StatDtoRequest statDtoRequest) {
        log.info("Find stats by {}", statDtoRequest);
        return statsService.findStats(statDtoRequest);
    }
}
