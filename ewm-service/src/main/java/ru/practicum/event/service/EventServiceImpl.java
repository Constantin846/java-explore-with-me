package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageParams;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.contexts.AdminFindEventsParams;
import ru.practicum.event.contexts.FindEventsParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.dto.mapper.EventDtoMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.SortEnum;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.EventSpecifications;
import ru.practicum.exceptions.ConditionNotMetException;
import ru.practicum.exceptions.NotAccessException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.InstantStringMapper;
import ru.practicum.user.User;
import ru.practicum.user.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
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
    private final InstantStringMapper instantMapper;

    @Override
    public List<EventShortDto> findAllByUserId(long userId, PageParams params) {
        List<Event> events = eventRepository.findAllByInitiator(
                userId, PageRequest.of(params.getFrom(), params.getSize()));
        return mapper.toEventShortDto(events);
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
                    return mapper.toEventFullDto(getEventById(event.getId()));
                }
            }
        }
        return null;
    }

    @Override
    public EventFullDto findByEventIdUserId(long eventId, long userId) {
        Event event = getEventById(eventId);
        if (checkUserAccess(event, userId)) {
            return mapper.toEventFullDto(event);
        }
        return null;
    }

    @Override
    @Transactional
    public EventFullDto updateByEventIdUserId(long eventId, long userId, UpdateEventRequest eventDto) {
        Event oldEvent = getEventById(eventId);
        if (oldEvent.getState() != State.PENDING && oldEvent.getState() != State.CANCELED) {
            String message = String.format(
                    "Only pending or canceled events can be changed. Event stats: %s", oldEvent.getState());
            log.warn(message);
            throw new ConditionNotMetException(message);
        }

        if (checkUserExists(userId)) {
            if (checkUserAccess(oldEvent, userId)) {
                if (Objects.isNull(eventDto.getCategory()) || checkCategoryExists(eventDto.getCategory())) {
                    if (Objects.isNull(eventDto.getEventDate())
                            && checkEventDateAvailable(oldEvent.getEventDate(), TWO_HOURS)
                            || checkEventDateAvailable(eventDto.getEventDate(), TWO_HOURS)) {

                        Event event = mapper.toEvent(eventDto);
                        event = eventRepository.save(event);
                        return mapper.toEventFullDto(getEventById(event.getId()));
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<EventFullDto> findAllForAdmin(AdminFindEventsParams params) {
        List<Specification<Event>> specifications = new ArrayList<>();

        if (Objects.nonNull(params.getUsers())) {
            specifications.add(EventSpecifications.hasUserIdEquals(params.getUsers()));
        }
        if (Objects.nonNull(params.getStates())) {
            specifications.add(EventSpecifications.hasStateEquals(params.getStates()));
        }
        if (Objects.nonNull(params.getCategories())) {
            specifications.add(EventSpecifications.hasCategoryIdEquals(params.getCategories()));
        }
        if (Objects.nonNull(params.getRangeStart())) {
            specifications.add(EventSpecifications.hasEventDateAfter(
                    instantMapper.toInstant(params.getRangeStart())));
        } else {
            specifications.add(EventSpecifications.hasEventDateAfter(Instant.now()));
        }
        if (Objects.nonNull(params.getRangeEnd())) {
            specifications.add(EventSpecifications.hasEventDateBefore(
                    instantMapper.toInstant(params.getRangeEnd())));
        }
        Specification<Event> allSpecifications = specifications.stream().reduce(Specification::and).get();

        List<Event> events = eventRepository.findAllBy(
                allSpecifications, PageRequest.of(params.getFrom(), params.getSize()));
        return mapper.toEventFullDto(events);
    }

    @Override
    @Transactional
    public EventFullDto updateByEventIdAdmin(long eventId, UpdateEventRequest eventDto) {
        Event oldEvent = getEventById(eventId);
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
                return mapper.toEventFullDto(getEventById(event.getId()));
            }
        }
        return null;
    }

    @Override
    public List<EventShortDto> findAll(FindEventsParams params) { // todo
        List<Specification<Event>> specifications = new ArrayList<>();

        if (Objects.nonNull(params.getCategories())) {
            specifications.add(EventSpecifications.hasCategoryIdEquals(params.getCategories()));
        }
        if (Objects.nonNull(params.getRangeStart())) {
            specifications.add(EventSpecifications.hasEventDateAfter(
                    instantMapper.toInstant(params.getRangeStart())));
        } else {
            specifications.add(EventSpecifications.hasEventDateAfter(Instant.now()));
        }
        if (Objects.nonNull(params.getRangeEnd())) {
            specifications.add(EventSpecifications.hasEventDateBefore(
                    instantMapper.toInstant(params.getRangeEnd())));
        }
        if (Objects.nonNull(params.getText())) {
            specifications.add(EventSpecifications.hasTextInAnnotation(params.getText()));
            specifications.add(EventSpecifications.hasTextInDescription(params.getText()));
        }
        if (Objects.nonNull(params.getPaid())) {
            specifications.add(EventSpecifications.hasPaidIsTrue(params.getPaid()));
        }
        if (Objects.nonNull(params.getOnlyAvailable()) && params.getOnlyAvailable()) {
            specifications.add(EventSpecifications.hasAvailableIsTrue());
        }
        Specification<Event> allSpecifications = specifications.stream().reduce(Specification::and).get();

        Sort sort = null;
        switch (SortEnum.valueOf(params.getSort())) {
            case EVENT_DATE -> sort = Sort.by(Sort.Direction.ASC, "event_date");

            case VIEWS -> sort = Sort.by(Sort.Direction.DESC, "views");

            default -> sort = Sort.unsorted();
        }

        List<Event> events = eventRepository.findAllBy(
                allSpecifications, PageRequest.of(params.getFrom(), params.getSize(), sort));
        eventRepository.incrementViewsEvents(events.stream().map(Event::getId).toList());
        return mapper.toEventShortDto(events);
    }

    @Override
    public EventFullDto findByEventId(long eventId) {
        Event event = getEventById(eventId);
        eventRepository.incrementViewsEvents(List.of(event.getId()));
        return mapper.toEventFullDto(event);
    }

    private Event getEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            String message = String.format("Event was not found by id: %s", eventId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private boolean checkUserAccess(Event event, long userId) {
        if (event.getInitiator().getId() == userId) return true;
        String message = String.format(
                "The user {id=%s} does not have access to update event: %s", userId, event.getId());
        log.warn(message);
        throw new NotAccessException(message);
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
