package ru.practicum.exceptions;

import lombok.Getter;

@Getter
public class StatsServiceException extends RuntimeException {
    private final int status;

    public StatsServiceException(int status, String message) {
        super(message);
        this.status = status;
    }
}
