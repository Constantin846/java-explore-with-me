package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.EventRepository;
import ru.practicum.event.contexts.AdminFindEventsParams;
import ru.practicum.event.contexts.FindEventsParams;
import ru.practicum.event.contexts.PageParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.dto.mapper.EventDtoMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.exceptions.ConditionNotMetException;
import ru.practicum.exceptions.NotAccessException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventDtoMapper mapper;

    @Override
    public List<EventShortDto> findAllByUserId(long userId, PageParams params) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto create(NewEventDto eventDto, long userId) {
        if (checkUserExists(userId)) {
            if (checkCategoryExists(eventDto.getCategory())) {
                if (checkEventDateAvailable(eventDto.getEventDate(), TWO_HOURS)) {

                    Event event = mapper.toEvent(eventDto);
                    User user = new User();
                    user.setId(userId);
                    event.setInitiator(user);
                    event = eventRepository.save(event);
                    return mapper.toEventFullDto(getEvent(event.getId()));
                }
            }
        }
        return null;
    }

    @Override
    public EventFullDto findByEventIdUserId(long eventId, long userId) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto updateByEventIdUserId(long eventId, long userId, UpdateEventRequest eventDto) {
        Event oldEvent = getEvent(eventId);
        if (oldEvent.getState() != State.PENDING && oldEvent.getState() != State.CANCELED) {
            String message = String.format(
                    "Only pending or canceled events can be changed. Event stats: %s", oldEvent.getState());
            log.warn(message);
            throw new ConditionNotMetException(message);
        }

        if (checkUserExists(userId)) {
            if (oldEvent.getInitiator().getId() != userId) {
                String message = String.format(
                        "The user {id=%s} does not have access to update event: %s", userId, eventId);
                log.warn(message);
                throw new NotAccessException(message);
            }

            if (Objects.isNull(eventDto.getCategory()) || checkCategoryExists(eventDto.getCategory())) {
                if (Objects.isNull(eventDto.getEventDate())
                        && checkEventDateAvailable(oldEvent.getEventDate(), TWO_HOURS)
                        || checkEventDateAvailable(eventDto.getEventDate(), TWO_HOURS)) {

                    Event event = mapper.toEvent(eventDto);
                    event = eventRepository.save(event);
                    return mapper.toEventFullDto(getEvent(event.getId()));
                }
            }
        }
        return null;
    }

    @Override
    public List<EventFullDto> findAllForAdmin(AdminFindEventsParams params) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto updateByEventIdAdmin(long eventId, UpdateEventRequest eventDto) {
        Event oldEvent = getEvent(eventId);
        if (Objects.isNull(eventDto.getCategory()) || checkCategoryExists(eventDto.getCategory())) {
            if (Objects.isNull(eventDto.getEventDate())
                    && checkEventDateAvailable(oldEvent.getEventDate(), ONE_HOUR)
                    || checkEventDateAvailable(eventDto.getEventDate(), ONE_HOUR)) {

                Event event = mapper.toEvent(eventDto);
                State oldEventState = oldEvent.getState();
                if (Objects.nonNull(eventDto.getStateAction())) {
                    switch (eventDto.getStateAction()) {
                        case PUBLISH_EVENT -> {
                            if (oldEventState != State.PENDING) {
                                String message = String.format(
                                        "Only pending events can be published. Event stats: %s", oldEventState);
                                log.warn(message);
                                throw new ConditionNotMetException(message);
                            }
                            event.setState(State.PUBLISHED);
                        }
                        case REJECT_EVENT -> {
                            if (oldEventState == State.PUBLISHED) {
                                String message = String.format(
                                        "Only not published events can be rejected. Event stats: %s", oldEventState);
                                log.warn(message);
                                throw new ConditionNotMetException(message);
                            }
                            event.setState(State.CANCELED);
                        }
                    }
                }
                event = eventRepository.save(event);
                return mapper.toEventFullDto(getEvent(event.getId()));
            }
        }
        return null;
    }

    @Override
    public List<EventShortDto> findAll(FindEventsParams params) {
        eventRepository.findAll(PageRequest.of(params.getFrom(), params.getSize())); //todo
        return null;
    }

    @Override
    public EventFullDto findByEventId(long eventId) {
        return null;
    }

    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            String message = String.format("Event was not found by id: %s", eventId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private boolean checkUserExists(long userId) {
        if (userRepository.existsById(userId)) return true;
        String message = String.format("User was not found by id: %s", userId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    private boolean checkCategoryExists(long userId) {
        if (categoryRepository.existsById(userId)) return true;
        String message = String.format("Category was not found by id: %s", userId);
        log.warn(message);
        throw new NotFoundException(message);
    }

    private static final int ONE_HOUR = 1;
    private static final int TWO_HOURS = 2;
    private static final long SECONDS_IN_HOUR = 3600;

    private boolean checkEventDateAvailable(Instant eventDate, int hoursCount) {
        Instant instant = Instant.now().plusSeconds(hoursCount * SECONDS_IN_HOUR);
        if (eventDate.isAfter(instant)) {
            String message = String.format(
                    "The event date must not be earlier than two hours away. Date: %s", eventDate);
            log.warn(message);
            throw new ConditionNotMetException(message);
        }
        return true;
    }
}
