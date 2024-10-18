package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.PageParams;
import ru.practicum.event.contexts.AdminFindEventsParams;
import ru.practicum.event.contexts.FindEventsParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.ewm.stats.dto.StatDtoResponse;
import ru.practicum.stats.StatsService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventServiceFacadeImpl implements EventServiceFacade {
    private final EventService eventService;
    private final StatsService statsService;

    @Override
    public List<EventShortDto> findAllByUserId(long userId, PageParams params) {
        return eventService.findAllByUserId(userId, params);
    }

    @Override
    public EventFullDto create(NewEventDto eventDto, long userId) {
        return eventService.create(eventDto, userId);
    }

    @Override
    public EventFullDto findByEventIdUserId(long eventId, long userId) {
        return eventService.findByEventIdUserId(eventId, userId);
    }

    @Override
    public EventFullDto updateByEventIdUserId(long eventId, long userId, UpdateEventRequest eventDto) {
        return eventService.updateByEventIdUserId(eventId, userId, eventDto);
    }

    @Override
    public List<EventFullDto> findAllForAdmin(AdminFindEventsParams params) {
        return eventService.findAllForAdmin(params);
    }

    @Override
    public EventFullDto updateByEventIdAdmin(long eventId, UpdateEventRequest eventDto) {
        return eventService.updateByEventIdAdmin(eventId, eventDto);
    }

    @Override
    public List<EventShortDto> findAll(FindEventsParams params, HttpServletRequest request) {
        statsService.create(request);
        return eventService.findAll(params);
    }

    @Override
    public EventFullDto findByEventId(long eventId, HttpServletRequest request) {
        List<StatDtoResponse> views = statsService.findStatForEvent(request);
        statsService.create(request);
        EventFullDto event = eventService.findByEventId(eventId);
        eventService.saveViews(eventId, views.size());
        event.setViews((long) views.size());
        return event;
    }
}
