package ru.practicum.event.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.LocationType;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.LocationDtoRequest;
import ru.practicum.location.dto.LocationTypeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventDtoMapper {
    EventDtoMapper MAPPER = Mappers.getMapper(EventDtoMapper.class);

    @Mapping(source = "location", target = "locationLat", qualifiedByName = "toLocationLat")
    @Mapping(source = "location", target = "locationLon", qualifiedByName = "toLocationLon")
    @Mapping(source = "location", target = "locationType", qualifiedByName = "toLocationType")
    @Mapping(target = "category", qualifiedByName = "toCategory")
    Event toEvent(NewEventDto newEventDto);

    @Mapping(source = "event", target = "location", qualifiedByName = "toLocationDto")
    EventFullDto toEventFullDto(Event event);

    EventShortDto toEventShortDto(Event event);

    default List<EventFullDto> toEventFullDto(List<Event> events) {
        return events.stream()
                .map(this::toEventFullDto)
                .toList();
    }

    default List<EventShortDto> toEventShortDto(List<Event> events) {
        return events.stream()
                .map(this::toEventShortDto)
                .toList();
    }

    @Named("toLocationDto")
    default LocationDto toLocation(Event event) {
        LocationTypeDto locationType = new LocationTypeDto();
        if (event.getLocationType() != null) {
            locationType.setId(event.getLocationType().getId());
            locationType.setName(event.getLocationType().getName());
        }
        LocationDto location = new LocationDto();
        location.setLat(event.getLocationLat());
        location.setLon(event.getLocationLon());
        location.setName(event.getLocationName());
        location.setLocationType(locationType);
        return location;
    }

    @Named("toLocationLat")
    default Double toLocationLat(LocationDtoRequest location) {
        if (location == null) return null;
        return location.getLat();
    }

    @Named("toLocationLon")
    default Double toLocationLon(LocationDtoRequest location) {
        if (location == null) return null;
        return location.getLon();
    }

    @Named("toLocationType")
    default LocationType toLocationType(LocationDtoRequest location) {
        if (location == null) return null;
        if (location.getLocationType() == null) return null;
        LocationType locationType = new LocationType();
        locationType.setId(location.getLocationType());
        return locationType;
    }

    @Named("toCategory")
    default Category toCategory(Long categoryId) {
        if (categoryId == null) return null;
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Named("toCategoryId")
    default long toCategoryId(Category category){
        return category.getId();
    }
}
