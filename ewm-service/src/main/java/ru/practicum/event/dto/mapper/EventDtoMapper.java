package ru.practicum.event.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.Location;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventDtoMapper {
    EventDtoMapper MAPPER = Mappers.getMapper(EventDtoMapper.class);

    @Mapping(source = "location", target = "locationLat", qualifiedByName = "toLocationLat")
    @Mapping(source = "location", target = "locationLon", qualifiedByName = "toLocationLon")
    @Mapping(target = "category", qualifiedByName = "toCategory")
    Event toEvent(NewEventDto newEventDto);

    @Mapping(source = "event", target = "location", qualifiedByName = "toLocation")
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

    @Named("toLocation")
    default Location toLocation(Event event) {
        Location location = new Location();
        location.setLat(event.getLocationLat());
        location.setLon(event.getLocationLon());
        return location;
    }

    @Named("toLocationLat")
    default double toLocationLat(Location location) {
        return location.getLat();
    }

    @Named("toLocationLon")
    default double toLocationLon(Location location) {
        return location.getLon();
    }

    @Named("toCategory")
    default Category toCategory(Long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Named("toCategoryId")
    default long toCategoryId(Category category) {
        return category.getId();
    }
}
