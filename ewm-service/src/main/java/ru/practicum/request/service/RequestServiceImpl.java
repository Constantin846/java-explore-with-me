package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotAccessException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.request.RequestRepository;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.dto.RequestStatusResponse;
import ru.practicum.request.dto.RequestStatusUpdate;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.Status;
import ru.practicum.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper mapper;

    @Override
    public List<RequestDto> findByUserId(long userId) {
        if (checkUserExists(userId)) {
            List<Request> requests = requestRepository.findAllByRequester(userId);
            return mapper.toRequestDto(requests);
        }
        return null;
    }

    @Override
    @Transactional
    public RequestDto create(long userId, long eventId) {
        if (checkUserExists(userId)) {
            Event event = getEventById(eventId);
            if (event.getInitiator().getId() == userId) {
                String message = String.format("Forbidden participate in your event: %s", userId);
                log.warn(message);
                throw new ConflictException(message);
            }
            if (event.getState() != State.PUBLISHED) {
                String message = String.format("Forbidden participate in unpublished event: %s", event);
                log.warn(message);
                throw new ConflictException(message);
            }
            if (event.getConfirmedRequests() == event.getParticipantLimit()
                    && event.getParticipantLimit() != UNLIMITED_PARTICIPANT) {
                String message = String.format("Forbidden: events participation request limit reached: %s", event);
                log.warn(message);
                throw new ConflictException(message);
            }

            Request request = new Request();
            request.setCreated(Instant.now());
            request.setEvent(event.getId());
            request.setRequester(userId);
            if (event.getParticipantLimit() != UNLIMITED_PARTICIPANT && event.getRequestModeration()) {
                request.setStatus(Status.PENDING);
            } else {
                request.setStatus(Status.CONFIRMED);
                incrementConfirmedRequests(event);
            }
            request = requestRepository.save(request);
            return mapper.toRequestDto(getRequestById(request.getId()));
        }
        return null;
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(long userId, long requestId) {
        if (checkUserExists(userId)) {
            Request request = getRequestById(requestId);

            if (request.getRequester() != userId) {
                String message = "Forbidden cancel someone else's request for participation";
                log.warn(message);
                throw new NotAccessException(message);
            }
            request.setStatus(Status.CANCELED);
            request = requestRepository.save(request);
            return mapper.toRequestDto(getRequestById(request.getId()));
        }
        return null;
    }

    @Override
    public List<RequestDto> findForUserByEventId(long userId, long eventId) {
        if (checkUserExists(userId)) {
            Event event = getEventById(eventId);
            if (checkUserAccessEvent(userId, event)) {
                List<Request> requests = requestRepository.findAllByEvent(eventId);
                return mapper.toRequestDto(requests);
            }
        }
        return null;
    }

    private static final int UNLIMITED_PARTICIPANT = 0;

    @Override
    @Transactional
    public RequestStatusResponse updateStatus(long userId, long eventId, RequestStatusUpdate requestStatus) {
        if (checkUserExists(userId)) {
            Event event = getEventById(eventId);
            if (checkUserAccessEvent(userId, event)) {
                List<Request> requests = requestRepository.findAllById(requestStatus.getRequestIds());

                if (requestStatus.getStatus() == Status.CONFIRMED) {
                    if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                        String message = String.format("Forbidden: events participation request limit reached: %s", event);
                        log.warn(message);
                        throw new ConflictException(message);
                    }

                    if (event.getParticipantLimit() == UNLIMITED_PARTICIPANT) {
                        requests = requests.stream()
                                .map(request -> {
                                    request.setStatus(Status.CONFIRMED);
                                    return request;
                                }).toList();
                    } else {
                        int confirmedRequests = event.getConfirmedRequests();
                        int participantLimit = event.getParticipantLimit();
                        for (Request request : requests) {
                            if (confirmedRequests < participantLimit) {
                                request.setStatus(Status.CONFIRMED);
                                confirmedRequests++;
                            } else {
                                request.setStatus(Status.REJECTED);
                            }
                        }
                        event.setConfirmedRequests(confirmedRequests);
                        eventRepository.save(event);
                    }
                } else if (requestStatus.getStatus() == Status.REJECTED) {
                    if (requests.stream().anyMatch(request -> request.getStatus() == Status.CONFIRMED)) {
                        String message = "Confirmed request must not reject";
                        log.warn(message);
                        throw new ConflictException(message);
                    }
                    requests = requests.stream()
                            .map(request -> {
                                request.setStatus(Status.REJECTED);
                                return request;
                            }).toList();
                    reduceConfirmedRequests(event, requests.size());
                }
                requestRepository.saveAll(requests);
                return toRequestStatusResponse(requests);
            }
        }
        return null;
    }

    private RequestStatusResponse toRequestStatusResponse(List<Request> requests) {
        Map<Status, Set<Request>> requestsMap = requests.stream()
                .collect(Collectors.groupingBy((Request::getStatus), Collectors.toSet()));

        RequestStatusResponse response = new RequestStatusResponse();
        response.setConfirmedRequests(mapper.toRequestDto(
                requestsMap.getOrDefault(Status.CONFIRMED, Set.of())));
        response.setRejectedRequests(mapper.toRequestDto(
                requestsMap.getOrDefault(Status.REJECTED, Set.of())));
        return response;
    }

    private Request getRequestById(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> {
            String message = String.format("Request was not found by id: %s", requestId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }

    private Event getEventById(long eventId) {
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

    private boolean checkUserAccessEvent(long userId, Event event) {
        if (event.getInitiator().getId() == userId) return true;
        String message = String.format("Not access to event: %s", event);
        log.warn(message);
        throw new NotAccessException(message);
    }

    private void incrementConfirmedRequests(Event event) {
        int confirmedRequests = event.getConfirmedRequests();
        event.setConfirmedRequests(++confirmedRequests);
        eventRepository.save(event);
    }

    private void reduceConfirmedRequests(Event event, int count) {
        int confirmedRequests = event.getConfirmedRequests() - count;
        event.setConfirmedRequests(confirmedRequests);
        eventRepository.save(event);
    }
}
