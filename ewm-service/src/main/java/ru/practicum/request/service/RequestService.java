package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestStatusResponse;
import ru.practicum.request.dto.RequestStatusUpdate;

import java.util.List;

public interface RequestService {
    List<RequestDto> findByUserId(long userId);

    RequestDto create(long userId, long eventId);

    RequestDto cancelRequest(long userId, long requestId);

    List<RequestDto> findForUserByEventId(long userId, long eventId);

    RequestStatusResponse updateStatus(long userId, long eventId, RequestStatusUpdate requestStatus);
}
