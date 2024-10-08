package ru.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoRequest;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.StatDtoMapper;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatsRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {
    @Mock
    private StatsRepository repository;
    @Mock
    private StatDtoMapper mapper;
    @InjectMocks
    private StatsServiceImpl service;

    @Test
    void create() {
        long id = 1L;
        Stat stat = new Stat();
        stat.setId(id);
        StatDto expectedStatDto = new StatDto();
        expectedStatDto.setId(id);
        when(mapper.toStat(expectedStatDto)).thenReturn(stat);
        when(repository.save(stat)).thenReturn(stat);
        when(repository.findById(stat.getId())).thenReturn(Optional.of(stat));
        when(mapper.toStatDto(stat)).thenReturn(expectedStatDto);

        StatDto actualStatDto = service.create(expectedStatDto);

        assertEquals(expectedStatDto.getId(), actualStatDto.getId());
    }

    @Test
    void create_whenStatHasNotFound_thenReturnNotFoundException() {
        long id = 1L;
        Stat stat = new Stat();
        stat.setId(id);
        StatDto statDto = new StatDto();
        statDto.setId(id);
        when(mapper.toStat(statDto)).thenReturn(stat);
        when(repository.save(stat)).thenReturn(stat);
        when(repository.findById(stat.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.create(statDto));
    }

    @Test
    void findStats_whenUrisIsEmptyAndUniqueIsTrue_thenCallRepo_findBetweenStartAndEndWithUniqueIp() {
        StatDtoRequest statDtoRequest = new StatDtoRequest();
        statDtoRequest.setStart(Instant.now());
        statDtoRequest.setEnd(Instant.now());
        statDtoRequest.setUnique(true);
        statDtoRequest.setUris(List.of());

        service.findStats(statDtoRequest);

        verify(repository).findBetweenStartAndEndWithUniqueIp(statDtoRequest.getStart(), statDtoRequest.getEnd());
    }

    @Test
    void findStats_whenUrisIsEmptyAndUniqueIsFalse_thenCallRepo_findBetweenStartAndEnd() {
        StatDtoRequest statDtoRequest = new StatDtoRequest();
        statDtoRequest.setStart(Instant.now());
        statDtoRequest.setEnd(Instant.now());
        statDtoRequest.setUnique(false);
        statDtoRequest.setUris(List.of());

        service.findStats(statDtoRequest);

        verify(repository).findBetweenStartAndEnd(statDtoRequest.getStart(), statDtoRequest.getEnd());
    }

    @Test
    void findStats_whenUrisIsNotEmptyAndUniqueIsTrue_thenCallRepo_findBetweenStartAndEndWithUniqueIpByUris() {
        StatDtoRequest statDtoRequest = new StatDtoRequest();
        statDtoRequest.setStart(Instant.now());
        statDtoRequest.setEnd(Instant.now());
        statDtoRequest.setUnique(true);
        statDtoRequest.setUris(List.of("uri"));

        service.findStats(statDtoRequest);

        verify(repository).findBetweenStartAndEndWithUniqueIpByUris(
                statDtoRequest.getStart(), statDtoRequest.getEnd(), statDtoRequest.getUris());
    }

    @Test
    void findStats_whenUrisIsNotEmptyAndUniqueIsFalse_thenCallRepo_findBetweenStartAndEndByUris() {
        StatDtoRequest statDtoRequest = new StatDtoRequest();
        statDtoRequest.setStart(Instant.now());
        statDtoRequest.setEnd(Instant.now());
        statDtoRequest.setUnique(false);
        statDtoRequest.setUris(List.of("uri"));

        service.findStats(statDtoRequest);

        verify(repository).findBetweenStartAndEndByUris(
                statDtoRequest.getStart(), statDtoRequest.getEnd(), statDtoRequest.getUris());
    }
}