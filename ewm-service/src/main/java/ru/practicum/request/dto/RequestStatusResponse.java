package ru.practicum.request.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatusResponse {

    Set<RequestDto> confirmedRequests;

    Set<RequestDto> rejectedRequests;
}
