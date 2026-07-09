package com.concert.backend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponse<T>(
        boolean success,
        int status,
        String message,
        T data,
        LocalDateTime timestamp
) {
}
