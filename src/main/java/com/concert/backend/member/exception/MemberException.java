package com.concert.backend.member.exception;

import com.concert.backend.common.exception.AppException;

public class MemberException extends AppException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }

    public MemberException(MemberErrorCode errorCode, Object... parameters) {
        super(errorCode, parameters);
    }
}

