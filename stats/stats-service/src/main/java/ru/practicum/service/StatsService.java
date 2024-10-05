package ru.practicum.service;

import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoRequest;
import ru.practicum.ewm.stats.dto.StatDtoResponse;

import java.util.List;

public interface StatsService {
    StatDto create(StatDto statDto);

    List<StatDtoResponse> findStats(StatDtoRequest statDtoRequest);
}
