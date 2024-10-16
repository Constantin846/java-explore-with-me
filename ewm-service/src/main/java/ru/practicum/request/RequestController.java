package ru.practicum.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestStatusResponse;
import ru.practicum.request.dto.RequestStatusUpdate;
import ru.practicum.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService requestService;
    private static final String EVENTS = "/events";
    private static final String EVENT_ID = "event-id";
    private static final String EVENT_ID_PATH = "/{event-id}";
    private static final String REQUESTS = "/requests";
    private static final String REQUEST_ID = "requests-id";
    private static final String REQUEST_ID_PATH = "/{requests-id}";
    private static final String USERS = "/users";
    private static final String USER_ID = "user-id";
    private static final String USER_ID_PATH = "/{user-id}";

    @GetMapping(USERS + USER_ID_PATH + REQUESTS)
    public List<RequestDto> findByUserId(@PathVariable(USER_ID) long userId) {
        log.info("Request: find requests by user id: {}", userId);
        return requestService.findByUserId(userId);
    }

    @PostMapping(USERS + USER_ID_PATH + REQUESTS)
    public RequestDto create(@PathVariable(USER_ID) long userId,
                             @RequestParam("eventId") long eventId) {
        log.info("Request: create request of user={} to take part in event: {}", userId, eventId);
        return requestService.create(userId, eventId);
    }

    @PatchMapping(USERS + USER_ID_PATH + REQUESTS + REQUEST_ID_PATH + "/cancel")
    public RequestDto cancel(@PathVariable(USER_ID) long userId,
                             @PathVariable(REQUEST_ID) long requestId) {
        log.info("Request: cancel request: {}", requestId);
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping(USERS + USER_ID_PATH + EVENTS + EVENT_ID_PATH + REQUESTS)
    public List<RequestDto> findForUserByEventId(@PathVariable(USER_ID) long userId,
                                                 @PathVariable(EVENT_ID) long eventId) {
        log.info("Request: find requests for user id:{} and event id:{}", userId, eventId);
        return requestService.findForUserByEventId(userId, eventId);
    }

    @PatchMapping(USERS + USER_ID_PATH + EVENTS + EVENT_ID_PATH + REQUESTS)
    public RequestStatusResponse updateStatus(@PathVariable(USER_ID) long userId,
                                              @PathVariable(EVENT_ID) long eventId,
                                              @Valid @RequestBody RequestStatusUpdate requestStatus) {
        log.info("Request: change status requests by user id:{} and event id:{}", userId, eventId);
        return requestService.updateStatus(userId, eventId, requestStatus);
    }
}
