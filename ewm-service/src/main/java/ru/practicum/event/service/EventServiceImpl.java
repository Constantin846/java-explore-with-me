package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageParams;
import ru.practicum.category.Category;
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
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConditionNotMetException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotAccessException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.location.LocationType;
import ru.practicum.location.LocationTypeRepository;
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
    private final LocationTypeRepository locationTypeRepository;
    private final UserRepository userRepository;
    private final EventDtoMapper mapper;
    private final InstantStringMapper instantMapper;

    @Override
    public List<EventShortDto> findAllByUserId(long userId, PageParams params) {
        Specification<Event> specification = EventSpecifications.hasUserIdEquals(List.of(userId));
        List<Event> events = eventRepository.findAll(
                specification, PageRequest.of(params.getFrom(), params.getSize())).stream().toList();
        return mapper.toEventShortDto(events);
    }

    @Override
    @Transactional
    public EventFullDto create(NewEventDto eventDto, long userId) {
        if (checkEventDateAvailable(eventDto.getEventDate(), TWO_HOURS)) {
            User user = getUserById(userId);
            Category category = getCategoryById(eventDto.getCategory());
            Event event = mapper.toEvent(eventDto);

            if (eventDto.getLocation().getLocationType() != null) {
                LocationType locationType = getLocationTypeByID(eventDto.getLocation().getLocationType());
                event.setLocationType(locationType);
            }

            event.setInitiator(user);
            event.setCategory(category);
            event.setState(State.PENDING);
            event.setCreatedOn(Instant.now());
            event = eventRepository.save(event);
            return mapper.toEventFullDto(getEventById(event.getId()));
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
            throw new ConflictException(message);
        }

        if (checkUserExists(userId)) {
            if (checkUserAccess(oldEvent, userId)) {
                if (Objects.isNull(eventDto.getCategory()) || checkCategoryExists(eventDto.getCategory())) {
                    if (Objects.isNull(eventDto.getEventDate())
                            && checkEventDateAvailable(oldEvent.getEventDate(), TWO_HOURS)
                            || checkEventDateAvailable(eventDto.getEventDate(), TWO_HOURS)) {

                        Event event = mapper.toEvent(eventDto);
                        if (Objects.nonNull(eventDto.getStateAction())) {
                            switch (eventDto.getStateAction()) {
                                case SEND_TO_REVIEW -> oldEvent.setState(State.PENDING);
                                case CANCEL_REVIEW -> oldEvent.setState(State.CANCELED);
                                default -> {
                                    String message = String.format("Not accept action: %s", eventDto.getStateAction());
                                    log.warn(message);
                                    throw new ConditionNotMetException(message);
                                }
                            }
                        }
                        event = updateProperties(oldEvent, event);
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
        if (Objects.nonNull(params.getRadius()) && Objects.nonNull(params.getLat())
                && Objects.nonNull(params.getLon())) {
            specifications.add(EventSpecifications.hasLocationInRadius(
                    params.getLat(), params.getLon(), params.getRadius()));
        }
        if (Objects.nonNull(params.getLocationNames())) {
            specifications.add(EventSpecifications.hasLocationNames(params.getLocationNames()));
        }
        if (Objects.nonNull(params.getLocationTypes())) {
            specifications.add(EventSpecifications.hasLocationTypeEquals(params.getLocationTypes()));
        }
        Specification<Event> allSpecifications = specifications.stream().reduce(Specification::and).get();

        List<Event> events = eventRepository.findAll(
                allSpecifications, PageRequest.of(params.getFrom(), params.getSize())).stream().toList();
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
                                throw new ConflictException(message);
                            }
                            oldEvent.setState(State.PUBLISHED);
                            oldEvent.setPublishedOn(Instant.now());
                        }
                        case REJECT_EVENT -> {
                            if (oldEventState == State.PUBLISHED) {
                                String message = String.format(
                                        "Only not published events can be rejected. Event stats: %s", oldEventState);
                                log.warn(message);
                                throw new ConflictException(message);
                            }
                            oldEvent.setState(State.CANCELED);
                        }
                        default -> {
                            String message = String.format("Not accept action: %s", eventDto.getStateAction());
                            log.warn(message);
                            throw new ConditionNotMetException(message);
                        }
                    }
                }
                event = updateProperties(oldEvent, event);
                event = eventRepository.save(event);
                return mapper.toEventFullDto(getEventById(event.getId()));
            }
        }
        return null;
    }

    @Override
    @Transactional
    public List<EventShortDto> findAll(FindEventsParams params) {
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
            specifications.add(EventSpecifications.hasTextInAnnotation(params.getText())
                            .or(EventSpecifications.hasTextInDescription(params.getText())));
        }
        if (Objects.nonNull(params.getPaid())) {
            specifications.add(EventSpecifications.hasPaidIsTrue(params.getPaid()));
        }
        if (Objects.nonNull(params.getOnlyAvailable()) && params.getOnlyAvailable()) {
            specifications.add(EventSpecifications.hasAvailableIsTrue());
        }
        if (Objects.nonNull(params.getRadius()) && Objects.nonNull(params.getLat())
                && Objects.nonNull(params.getLon())) {
            specifications.add(EventSpecifications.hasLocationInRadius(
                    params.getLat(), params.getLon(), params.getRadius()));
        }
        if (Objects.nonNull(params.getLocationNames())) {
            specifications.add(EventSpecifications.hasLocationNames(params.getLocationNames()));
        }
        if (Objects.nonNull(params.getLocationTypes())) {
            specifications.add(EventSpecifications.hasLocationTypeEquals(params.getLocationTypes()));
        }
        Specification<Event> allSpecifications = specifications.stream().reduce(Specification::and).get();

        Sort sort = getEventSort(params.getSort());
        List<Event> events = eventRepository.findAll(
                allSpecifications, PageRequest.of(params.getFrom(), params.getSize(), sort)).stream().toList();
        if (events.isEmpty()) {
            String message = "No events for this request";
            log.warn(message);
            throw new BadRequestException(message);
        }
        return mapper.toEventShortDto(events);
    }

    @Override
    @Transactional
    public EventFullDto findByEventId(long eventId) {
        Event event = getEventById(eventId);
        if (event.getState() != State.PUBLISHED) {
            String message = String.format("Not found published event: %s", eventId);
            log.warn(message);
            throw new NotFoundException(message);
        }
        return mapper.toEventFullDto(event);
    }

    @Override
    @Async
    @Transactional
    public void saveViews(long eventId, int views) {
        eventRepository.saveViews(eventId, views);
    }

    private Sort getEventSort(String sortEnum) {
        if (Objects.isNull(sortEnum)) return Sort.unsorted();
        return switch (SortEnum.valueOf(sortEnum)) {
            case EVENT_DATE ->  Sort.by(Sort.Direction.ASC, "eventDate");
            case VIEWS -> Sort.by(Sort.Direction.DESC, "views");
        };
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

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            String message = String.format("User was not found by id: %s", userId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private Category getCategoryById(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            String message = String.format("Category was not found by id: %s", catId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private LocationType getLocationTypeByID(long typeId) {
        return locationTypeRepository.findById(typeId).orElseThrow(() -> {
            String message = String.format("Location type was not found by id: %s", typeId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private Event updateProperties(Event oldEvent, Event event) {
        if (Objects.nonNull(event.getAnnotation())) {
            oldEvent.setAnnotation(event.getAnnotation());
        }
        if (Objects.nonNull(event.getCategory())) {
            oldEvent.setCategory(event.getCategory());
        }
        if (Objects.nonNull(event.getDescription())) {
            oldEvent.setDescription(event.getDescription());
        }
        if (Objects.nonNull(event.getEventDate())) {
            oldEvent.setEventDate(event.getEventDate());
        }
        if (Objects.nonNull(event.getInitiator())) {
            oldEvent.setInitiator(event.getInitiator());
        }
        if (Objects.nonNull(event.getLocationLat())) {
            oldEvent.setLocationLat(event.getLocationLat());
        }
        if (Objects.nonNull(event.getLocationLon())) {
            oldEvent.setLocationLon(event.getLocationLon());
        }
        if (Objects.nonNull(event.getLocationName())) {
            oldEvent.setLocationName(event.getLocationName());
        }
        if (Objects.nonNull(event.getLocationType())) {
            long id = event.getLocationType().getId();
            oldEvent.setLocationType(getLocationTypeByID(id));
        }
        if (Objects.nonNull(event.getPaid())) {
            oldEvent.setPaid(event.getPaid());
        }
        if (Objects.nonNull(event.getParticipantLimit())) {
            oldEvent.setParticipantLimit(event.getParticipantLimit());
        }
        if (Objects.nonNull(event.getRequestModeration())) {
            oldEvent.setRequestModeration(event.getRequestModeration());
        }
        if (Objects.nonNull(event.getTitle())) {
            oldEvent.setTitle(event.getTitle());
        }
        return oldEvent;
    }

    private static final int ONE_HOUR = 1;
    private static final int TWO_HOURS = 2;
    private static final long SECONDS_IN_HOUR = 3600;

    private boolean checkEventDateAvailable(Instant eventDate, int hoursCount) {
        Instant instant = Instant.now().plusSeconds(hoursCount * SECONDS_IN_HOUR);
        if (eventDate.isBefore(instant)) {
            String message = String.format(
                    "The event date must not be earlier than %d hours away. Date: %s", hoursCount, eventDate);
            log.warn(message);
            throw new BadRequestException(message);
        }
        return true;
    }
}
