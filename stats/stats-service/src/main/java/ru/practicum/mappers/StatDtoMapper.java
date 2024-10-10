package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.model.Stat;

@Mapper(componentModel = "spring")
public interface StatDtoMapper {
    StatDtoMapper MAPPER = Mappers.getMapper(StatDtoMapper.class);

    StatDto toStatDto(Stat stat);

    Stat toStat(StatDto statDto);
}
