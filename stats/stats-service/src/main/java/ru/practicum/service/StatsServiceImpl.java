package ru.practicum.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoRequest;
import ru.practicum.ewm.stats.dto.StatDtoResponse;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.StatDtoMapper;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatsRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class StatsServiceImpl implements StatsService {
    private final StatDtoMapper mapper;
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public StatDto create(StatDto statDto) {
        Stat stat = mapper.toStat(statDto);
        stat = statsRepository.save(stat);
        return mapper.toStatDto(getById(stat.getId()));
    }

    @Override
    public List<StatDtoResponse> findStats(@Valid StatDtoRequest statDtoRequest) {
        if (statDtoRequest.getUris() == null || statDtoRequest.getUris().isEmpty()) {
            if (statDtoRequest.isUnique()) {
                return statsRepository.findBetweenStartAndEndWithUniqueIp(
                        statDtoRequest.getStart(),
                        statDtoRequest.getEnd());

            } else {
                return statsRepository.findBetweenStartAndEnd(
                        statDtoRequest.getStart(),
                        statDtoRequest.getEnd());
            }
        } else {
            if (statDtoRequest.isUnique()) {
                return statsRepository.findBetweenStartAndEndWithUniqueIpByUris(
                        statDtoRequest.getStart(),
                        statDtoRequest.getEnd(),
                        statDtoRequest.getUris());

            } else {
                return statsRepository.findBetweenStartAndEndByUris(
                        statDtoRequest.getStart(),
                        statDtoRequest.getEnd(),
                        statDtoRequest.getUris());
            }
        }
    }

    private Stat getById(long statId) {
        Optional<Stat> statOp = statsRepository.findById(statId);
        return statOp.orElseThrow(() -> {
            String message = String.format("Stat was not found by id: %d", statId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }
}
