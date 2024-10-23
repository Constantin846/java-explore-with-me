package ru.practicum.location.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.location.LocationType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationTypeDtoMapper {
    LocationTypeDtoMapper MAPPER = Mappers.getMapper(LocationTypeDtoMapper.class);

    LocationType toLocationType(LocationTypeDto dto);

    LocationTypeDto toLocationTypeDto(LocationType type);

    default List<LocationTypeDto> toLocationTypeDto(List<LocationType> types) {
        return types.stream()
                .map(this::toLocationTypeDto)
                .toList();
    }
}
