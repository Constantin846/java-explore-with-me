package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ErrorHandler {
    private static final String ERROR = "error";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handlerNotFoundException(final NotFoundException e) {
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidException(final MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : INTERNAL_SERVER_ERROR;
        message = message != null ? message : INTERNAL_SERVER_ERROR;
        return Map.of(ERROR, message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerThrowable(final Throwable e) {
        String message = INTERNAL_SERVER_ERROR;
        log.warn(message, e);
        return Map.of(ERROR, message);
    }
}