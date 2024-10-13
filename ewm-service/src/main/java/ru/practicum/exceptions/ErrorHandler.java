package ru.practicum.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.mappers.InstantStringMapper;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
@RequiredArgsConstructor
public class ErrorHandler {
    private final InstantStringMapper instantStringMapper;
    private static final String ERROR = "error";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    @ExceptionHandler
    public ResponseEntity<Object> handlerStatsServiceException(final StatsServiceException e) {
        String[] message = e.getMessage()
                .replace("{", "") //todo
                .replace("}", "")
                .split(":");

        return ResponseEntity
                .status(e.getStatus())
                .body(Map.of(ERROR, Map.of(
                        message[0], Map.of(
                                message[1], Map.of(
                                        message[2], message[3]
                                )
                        )
                )));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerNotFoundException(final NotFoundException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND.toString());
        apiError.setReason("The required object was not found");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerRuntimeException(final RuntimeException e) {
        return Map.of(ERROR, e.getMessage()); //todo
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerValidException(final MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<StringBuilder> messages = fieldErrors.stream()
                .map(fieldError -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Field: ").append(fieldError.getField())
                            .append(". Error: ").append(fieldError.getDefaultMessage())
                            .append(". Value: ").append(fieldError.getRejectedValue())
                            .append(".");
                    return sb;
                }).toList();
        String message = String.join(" & ", messages);

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.toString());
        apiError.setReason("Incorrectly made request.");
        apiError.setMessage(message);
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerThrowable(final Throwable e) {
        String message = INTERNAL_SERVER_ERROR; //todo
        log.warn(message, e);
        return Map.of(ERROR, message);
    }
}