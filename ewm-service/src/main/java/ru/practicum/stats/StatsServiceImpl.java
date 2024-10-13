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

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    @Value("${ewm-service.app}")
    private String app;
    private final StatsClient statsClient;

    @Override
    @Async
    public CompletableFuture<StatDto> create(HttpServletRequest request) {
        System.out.println(Thread.currentThread().getName()); //todo
        StatDto statDto = new StatDto();
        statDto.setApp(this.app);
        statDto.setUri(request.getRequestURI());
        statDto.setIp(request.getRemoteAddr());
        statDto.setTimestamp(Instant.now());
        log.info("Send request to create stat: {}", statDto);
        return CompletableFuture.completedFuture(statsClient.create(statDto));
    }

    @Override
    public List<StatDtoResponse> findStats(StatsRequestContext statsRequestContext) {
        log.info("Send request to find stats: {}", statsRequestContext); //todo
        return statsClient.findStats(statsRequestContext);
    }
}
