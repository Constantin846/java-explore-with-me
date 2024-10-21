package ru.practicum.location.service;

import ru.practicum.PageParams;
import ru.practicum.location.dto.LocationTypeDto;

import java.util.List;

public interface LocationTypeService {
    LocationTypeDto create(LocationTypeDto locationTypeDto);

    LocationTypeDto update(LocationTypeDto locationTypeDto, long typeId);

    void delete(long typeId);

    List<LocationTypeDto> findAll(PageParams params);

    LocationTypeDto findById(long typeId);
}
