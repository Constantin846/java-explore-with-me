package ru.practicum.exceptions;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {

    String message;

    String reason;

    String status;

    String timestamp;
}
