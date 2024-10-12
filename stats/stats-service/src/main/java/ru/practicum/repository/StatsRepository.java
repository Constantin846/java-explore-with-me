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
    @Query(value = """
            select new ru.practicum.ewm.stats.dto.StatDtoResponse(s.app, s.uri, count(id))
            from Stat as s
            where s.timestamp > :start
            and s.timestamp < :end
            group by uri, app
            order by count(id) desc
            """)
    List<StatDtoResponse> findBetweenStartAndEnd(
            @Param("start") Instant start,
            @Param("end") Instant end);

    @Query(value = """
            select new ru.practicum.ewm.stats.dto.StatDtoResponse(s.st_app, s.st_uri, count(s.st_ip))
            from (select st.app as st_app, st.uri as st_uri, st.ip as st_ip
                    from ru.practicum.model.Stat as st
                    where st.timestamp > :start
                    and st.timestamp < :end
                    group by st.uri, st.app, st.ip
                    ) as s
            group by s.st_uri, s.st_app
            order by count(s.st_ip) desc
            """)
    List<StatDtoResponse> findBetweenStartAndEndWithUniqueIp(
            @Param("start") Instant start,
            @Param("end") Instant end);

    @Query(value = """
            select new ru.practicum.ewm.stats.dto.StatDtoResponse(s.app, s.uri, count(id))
            from Stat as s
            where s.timestamp > :start
            and s.timestamp < :end
            and s.uri in :uris
            group by uri, app
            order by count(id) desc
            """)
    List<StatDtoResponse> findBetweenStartAndEndByUris(
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("uris") Collection<String> uris);

    @Query(value = """
            select new ru.practicum.ewm.stats.dto.StatDtoResponse(s.st_app, s.st_uri, count(s.st_ip))
            from (select st.app as st_app, st.uri as st_uri, st.ip as st_ip
                    from ru.practicum.model.Stat as st
                    where st.timestamp > :start
                    and st.timestamp < :end
                    group by st.uri, st.app, st.ip
                    ) as s
            where s.st_uri in :uris
            group by s.st_uri, s.st_app
            order by count(s.st_ip) desc
            """)
    List<StatDtoResponse> findBetweenStartAndEndWithUniqueIpByUris(
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("uris") Collection<String> uris);
}
