package ru.practicum.event.service;

import ru.practicum.PageParams;
import ru.practicum.event.contexts.AdminFindEventsParams;
import ru.practicum.event.contexts.FindEventsParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;

import java.util.List;

public interface EventService {
    List<EventShortDto> findAllByUserId(long userId, PageParams params);

    EventFullDto create(NewEventDto eventDto, long userId);

    EventFullDto findByEventIdUserId(long eventId, long userId);

    EventFullDto updateByEventIdUserId(long eventId, long userId, UpdateEventRequest eventDto);

    List<EventFullDto> findAllForAdmin(AdminFindEventsParams params);

    EventFullDto updateByEventIdAdmin(long eventId, UpdateEventRequest eventDto);

    List<EventShortDto> findAll(FindEventsParams params);

    EventFullDto findByEventId(long eventId);

    void saveViews(long eventId, int views);
}
