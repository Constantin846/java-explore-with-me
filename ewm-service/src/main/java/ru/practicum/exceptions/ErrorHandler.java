package ru.practicum.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.mappers.InstantStringMapper;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
@RequiredArgsConstructor
public class ErrorHandler {
    private final InstantStringMapper instantStringMapper;
    private static final String ERROR = "error";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";
    private static final String THREW_EXCEPTION = "Threw exception: %s";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handlerStatsServiceException(final StatsServiceException e) {
        log.warn(String.format(THREW_EXCEPTION, e));
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        apiError.setReason("Stat server error");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerBadRequestException(final BadRequestException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.toString());
        apiError.setReason("The bad requested");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handlerConditionNotMetException(final ConditionNotMetException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.FORBIDDEN.toString());
        apiError.setReason("For the requested operation the conditions are not met");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.FORBIDDEN.toString());
        apiError.setReason("For the requested operation the request parameters are not met");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handlerRuntimeException(final RuntimeException e) {
        log.warn(String.format(THREW_EXCEPTION, e));
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        apiError.setReason("Runtime exception: something went wrong");
        apiError.setMessage(INTERNAL_SERVER_ERROR);
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.warn(String.format(THREW_EXCEPTION, e));
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT.toString());
        apiError.setReason("Constraint violation exception during save request to database");
        apiError.setMessage("Conflict data exception");
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerConflictException(final ConflictException e) {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT.toString());
        apiError.setReason("Conflict case the conditions are not met");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handlerSQLException(final SQLException e) {
        log.warn(String.format("Exception during request to database: %s", e));
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        apiError.setReason("Exception during request to database");
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
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
    public ApiError handlerThrowable(final Throwable e) {
        log.warn(INTERNAL_SERVER_ERROR, e);
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        apiError.setReason("Undefined server exception");
        apiError.setMessage(INTERNAL_SERVER_ERROR);
        apiError.setTimestamp(instantStringMapper.toString(Instant.now()));
        return apiError;
    }
}