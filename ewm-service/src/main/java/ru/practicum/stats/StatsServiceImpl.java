package ru.practicum.stats;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoResponse;
import ru.practicum.ewm.stats.dto.StatsRequestContext;
import ru.practicum.mappers.InstantStringMapper;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    @Value("${ewm-service.app}")
    private String app;
    @Value("${ewm-service.date-start-app}")
    private String dateStartApp;
    private final StatsClient statsClient;
    private final InstantStringMapper instantStringMapper;

    @Override
    @Async
    public CompletableFuture<StatDto> create(HttpServletRequest request) {
        StatDto statDto = new StatDto();
        statDto.setApp(this.app);
        statDto.setUri(request.getRequestURI());
        statDto.setIp(request.getRemoteAddr());
        statDto.setTimestamp(Instant.now());
        log.info("Send request to create stat: {}", statDto);
        return CompletableFuture.completedFuture(statsClient.create(statDto));
    }

    @Override
    public List<StatDtoResponse> findStatForEvent(HttpServletRequest request) {
        StatsRequestContext context = new StatsRequestContext();
        context.setStart(dateStartApp);
        context.setEnd(instantStringMapper.toString(Instant.now().plusSeconds(60)));
        context.setUris(List.of(request.getRequestURI()));
        context.setUnique(true);
        return findStats(context);
    }

    @Override
    public List<StatDtoResponse> findStats(StatsRequestContext statsRequestContext) {
        log.info("Send request to find stats: {}", statsRequestContext);
        return statsClient.findStats(statsRequestContext);
    }
}
