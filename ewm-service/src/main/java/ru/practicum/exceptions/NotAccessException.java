package ru.practicum.exceptions;

public class NotAccessException extends RuntimeException {
    public NotAccessException(String message) {
        super(message);
    }
}
