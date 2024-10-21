package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageParams;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.location.LocationType;
import ru.practicum.location.LocationTypeRepository;
import ru.practicum.location.dto.LocationTypeDto;
import ru.practicum.location.dto.LocationTypeDtoMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationTypeServiceImpl implements LocationTypeService {
    private static final String LOCATION_TYPE_WAS_NOT_FOUND_BY_ID_S = "Location type was not found by id: %s";
    private final LocationTypeRepository locationTypeRepository;
    private final LocationTypeDtoMapper mapper;

    @Override
    @Transactional
    public LocationTypeDto create(LocationTypeDto locationTypeDto) {
        return save(locationTypeDto);
    }

    @Override
    @Transactional
    public LocationTypeDto update(LocationTypeDto locationTypeDto, long typeId) {
        LocationType oldType = getTypeById(typeId);
        locationTypeDto.setId(oldType.getId());
        return save(locationTypeDto);
    }

    @Override
    @Transactional
    public void delete(long typeId) {
        if (checkLocationTypeExists(typeId)) {
            locationTypeRepository.deleteById(typeId);
        }
    }

    @Override
    public List<LocationTypeDto> findAll(PageParams params) {
        List<LocationType> types = locationTypeRepository.findAllBy(PageRequest.of(params.getFrom(), params.getSize()));
        return mapper.toLocationTypeDto(types);
    }

    @Override
    public LocationTypeDto findById(long typeId) {
        return mapper.toLocationTypeDto(getTypeById(typeId));
    }

    private LocationTypeDto save(LocationTypeDto locationTypeDto) {
        LocationType locationType = mapper.toLocationType(locationTypeDto);
        locationType = locationTypeRepository.save(locationType);
        return mapper.toLocationTypeDto(getTypeById(locationType.getId()));
    }

    private LocationType getTypeById(long typeId) {
        return locationTypeRepository.findById(typeId).orElseThrow(() -> {
            String message = String.format(LOCATION_TYPE_WAS_NOT_FOUND_BY_ID_S, typeId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private boolean checkLocationTypeExists(long typeId) {
        if (locationTypeRepository.existsById(typeId)) return true;
        String message = String.format(LOCATION_TYPE_WAS_NOT_FOUND_BY_ID_S, typeId);
        log.warn(message);
        throw new NotFoundException(message);
    }
}
