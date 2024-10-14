package ru.practicum.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.PageParams;
import ru.practicum.event.contexts.AdminFindEventsParams;
import ru.practicum.event.contexts.FindEventsParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.service.EventServiceFacade;
import ru.practicum.validation.groups.CreateValid;
import ru.practicum.validation.groups.UpdateValid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventServiceFacade eventServiceFacade;
    private static final String ADMIN = "/admin";
    private static final String EVENTS = "/events";
    private static final String EVENT_ID = "/{event-id}";
    private static final String USERS = "/users";
    private static final String USER_ID = "/{user-id}";

    @GetMapping(USERS + USER_ID + EVENTS)
    public List<EventShortDto> findAllByUserId(@PathVariable(USER_ID) long userId,
                                            @Valid PageParams params) {
        log.info("Request: find all by user id={} with params={}", userId, params);
        return eventServiceFacade.findAllByUserId(userId, params);
    }

    @PostMapping(USERS + USER_ID + EVENTS)
    public EventFullDto create(@PathVariable(USER_ID) long userId,
                                @Validated({CreateValid.class})  @RequestBody NewEventDto eventDto) {
        log.info("Request: create event: {}", eventDto);
        return eventServiceFacade.create(eventDto, userId);
    }

    @GetMapping(USERS + USER_ID + EVENTS + EVENT_ID)
    public EventFullDto findByEventIdUserId(@PathVariable(USER_ID) long userId,
                                            @PathVariable(EVENT_ID) long eventId) {
        log.info("Request: find by event id={} with user id={}", eventId, userId);
        return eventServiceFacade.findByEventIdUserId(eventId, userId);
    }

    @PatchMapping(USERS + USER_ID + EVENTS + EVENT_ID)
    public EventFullDto updateByEventIdUserId(
            @PathVariable(USER_ID) long userId,
            @PathVariable(EVENT_ID) long eventId,
            @Validated({UpdateValid.class}) @RequestBody UpdateEventRequest eventDto) {
        log.info("Request: update event by event id={} with user id={}", eventId, userId);
        return eventServiceFacade.updateByEventIdUserId(eventId, userId, eventDto);
    }

    @GetMapping(ADMIN + EVENTS)
    public List<EventFullDto> findAllForAdmin(@Valid AdminFindEventsParams params) {
        log.info("Request: find events for admin by params={}", params);
        return eventServiceFacade.findAllForAdmin(params);
    }

    @PatchMapping(ADMIN+ EVENTS + EVENT_ID)
    public EventFullDto updateByEventIdAdmin(
            @PathVariable(EVENT_ID) long eventId,
            @RequestBody UpdateEventRequest eventDto) {
        log.info("Request: update event by admin with event id={}", eventId);
        return eventServiceFacade.updateByEventIdAdmin(eventId, eventDto);
    }

    @GetMapping(EVENTS)
    public List<EventShortDto> findAll(@Valid FindEventsParams params, HttpServletRequest request) {
        log.info("Request: find events by params={}", params);
        return eventServiceFacade.findAll(params, request);
    }

    @GetMapping(EVENTS + EVENT_ID)
    public EventFullDto findByEventId(@PathVariable(EVENT_ID) long eventId, HttpServletRequest request) {
        log.info("Request: find by event id={}", eventId);
        return eventServiceFacade.findByEventId(eventId, request);
    }
}
