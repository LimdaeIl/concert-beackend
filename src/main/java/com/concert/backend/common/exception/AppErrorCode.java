package com.concert.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AppErrorCode implements ErrorCode {


    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Invalid request data."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method is not allowed."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested resource was not found."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Authentication is required."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");


    private final HttpStatus httpStatus;
    private final String messageTemplate;

    @Override
    public HttpStatus status() {
        return httpStatus;
    }

    @Override
    public String message() {
        return messageTemplate;
    }
}
