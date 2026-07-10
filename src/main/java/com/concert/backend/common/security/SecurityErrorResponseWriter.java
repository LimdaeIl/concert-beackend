package com.concert.backend.common.security;

import com.concert.backend.common.config.RequestTraceFilter;
import com.concert.backend.common.response.CommonResponse;
import com.concert.backend.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SecurityErrorResponseWriter {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final ObjectMapper objectMapper;
    private final Clock clock;

    public void write(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String errorCode,
            String detail,
            String message
    ) throws IOException {
        String traceId = MDC.get(RequestTraceFilter.TRACE_ID);

        ErrorResponse errorResponse = ErrorResponse.of(
                problemType(errorCode),
                errorCode,
                status,
                detail,
                request.getRequestURI(),
                errorCode,
                traceId,
                null,
                null
        );

        CommonResponse<ErrorResponse> body = CommonResponse.failure(
                status.value(),
                message,
                errorResponse,
                LocalDateTime.ofInstant(clock.instant(), SEOUL)
        );

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), body);
    }

    private String problemType(String errorCode) {
        return "about:blank/"
                + errorCode.toLowerCase().replace('_', '-');
    }
}
