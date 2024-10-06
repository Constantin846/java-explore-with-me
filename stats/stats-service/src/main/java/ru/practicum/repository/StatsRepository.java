package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.dto.StatDtoResponse;
import ru.practicum.model.Stat;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {
    String SELECT_LIST_OF_STAT_DTO_RESPONSE_BETWEEN_START_AND_END_BY_URIS = """
            select new ru.practicum.ewm.stats.dto.StatDtoResponse(s.app, s.uri, count(id))
            from Stat as s
            where s.timestamp > :start
            and s.timestamp < :end
            and s.uri in :uris
            group by uri, app
            """;

    @Query(value = SELECT_LIST_OF_STAT_DTO_RESPONSE_BETWEEN_START_AND_END_BY_URIS)
    List<StatDtoResponse> findBetweenStartAndEndByUris(
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("uris") Collection<String> uris);

    @Query(value = SELECT_LIST_OF_STAT_DTO_RESPONSE_BETWEEN_START_AND_END_BY_URIS + ", ip")
    List<StatDtoResponse> findBetweenStartAndEndWithUniqueIpByUris(
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("uris") Collection<String> uris);
}
