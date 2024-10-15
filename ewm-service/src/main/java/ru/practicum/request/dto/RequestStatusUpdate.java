package ru.practicum.request.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.request.model.Status;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatusUpdate {

    List<Long> requestIds;

    Status status;
}
