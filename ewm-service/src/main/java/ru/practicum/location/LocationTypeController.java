package ru.practicum.location;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.PageParams;
import ru.practicum.location.dto.LocationTypeDto;
import ru.practicum.location.service.LocationTypeService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class LocationTypeController {
    private final LocationTypeService locationTypeService;
    private static final String ADMIN = "/admin";
    private static final String LOCATION_TYPES = "/location/types";
    private static final String TYPE_ID = "type-id";
    private static final String TYPE_ID_PATH = "/{type-id}";

    @PostMapping(ADMIN + LOCATION_TYPES)
    @ResponseStatus(HttpStatus.CREATED)
    public LocationTypeDto create(@Valid @RequestBody LocationTypeDto locationTypeDto) {
        log.info("Request: create location type: {}", locationTypeDto);
        return locationTypeService.create(locationTypeDto);
    }

    @PatchMapping(ADMIN + LOCATION_TYPES + TYPE_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public LocationTypeDto update(@Valid @RequestBody LocationTypeDto locationTypeDto,
                              @PathVariable(TYPE_ID) long typeId) {
        log.info("Request: update location type(id={}): {}", typeId, locationTypeDto);
        return locationTypeService.update(locationTypeDto, typeId);
    }

    @DeleteMapping(ADMIN + LOCATION_TYPES + TYPE_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(TYPE_ID) long typeId) {
        log.info("Request: delete location type by id: {}", typeId);
        locationTypeService.delete(typeId);
    }

    @GetMapping(LOCATION_TYPES)
    @ResponseStatus(HttpStatus.OK)
    public List<LocationTypeDto> findAll(@Valid PageParams params) {
        log.info("Request: find location type by params: {}", params);
        return locationTypeService.findAll(params);
    }

    @GetMapping(LOCATION_TYPES + TYPE_ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public LocationTypeDto findById(@PathVariable(TYPE_ID) long typeId) {
        log.info("Request: find location type by id: {}", typeId);
        return locationTypeService.findById(typeId);
    }
}
