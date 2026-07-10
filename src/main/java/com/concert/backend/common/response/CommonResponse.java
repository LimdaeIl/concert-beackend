package com.concert.backend.common.response;

import java.time.LocalDateTime;

public record CommonResponse<T>(
        boolean success,
        int status,
        String message,
        T data,
        LocalDateTime timestamp
) {

    public static <T> CommonResponse<T> success(
            int status,
            String message,
            T data,
            LocalDateTime timestamp
    ) {
        return new CommonResponse<>(
                true,
                status,
                message,
                data,
                timestamp
        );
    }

    public static <T> CommonResponse<T> failure(
            int status,
            String message,
            T data,
            LocalDateTime timestamp
    ) {
        return new CommonResponse<>(
                false,
                status,
                message,
                data,
                timestamp
        );
    }
}
