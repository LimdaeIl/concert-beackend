package com.concert.backend.member.exception;

import com.concert.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "member: This member '%s' does not exist."),
    DELETED_MEMBER(HttpStatus.UNAUTHORIZED, "member: This member '%s' has been deleted."),
    DEACTIVATE_MEMBER(HttpStatus.UNAUTHORIZED, "member: This member '%s' has been deactivated.");

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

