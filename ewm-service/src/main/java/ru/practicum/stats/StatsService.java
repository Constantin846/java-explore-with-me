package ru.practicum.stats;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoResponse;
import ru.practicum.ewm.stats.dto.StatsRequestContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface StatsService {
    CompletableFuture<StatDto> create(HttpServletRequest request);

    List<StatDtoResponse> findStatForEvent(HttpServletRequest request);

    List<StatDtoResponse> findStats(StatsRequestContext statsRequestContext);
}
