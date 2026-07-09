package com.concert.backend.auth.exception;

import com.concert.backend.common.exception.AppException;
import com.concert.backend.common.exception.ErrorCode;

public class AuthException extends AppException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, Object... parameters) {
        super(errorCode, parameters);
    }
}
