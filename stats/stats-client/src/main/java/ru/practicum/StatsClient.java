package ru.practicum;

import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoResponse;
import ru.practicum.ewm.stats.dto.StatsRequestContext;

import java.util.List;

public interface StatsClient {
    StatDto create(StatDto statDto);

    List<StatDtoResponse> findStats(StatsRequestContext statsRequestContext);
}
