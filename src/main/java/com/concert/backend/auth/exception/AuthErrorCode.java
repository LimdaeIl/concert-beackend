package com.concert.backend.auth.exception;

import com.concert.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "auth: The email '%s' is already registered."),
    DUPLICATE_PHONE(HttpStatus.CONFLICT, "auth: The phone number '%s' is already in use."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "auth: Expired Access Token."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "auth: Expired Refresh Token."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "auth: Invalid Access Token."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "auth: Invalid Refresh Token."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "auth: Invalid Token Type."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "auth: Member ID '%s' not found." ),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "auth: Invalid Password."),
    TOKEN_STORE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "auth: The token store is unavailable."),
    ;



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