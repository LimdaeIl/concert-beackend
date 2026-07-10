package com.concert.backend.common.exception;

import com.concert.backend.common.config.RequestTraceFilter;
import com.concert.backend.common.response.CommonResponse;
import com.concert.backend.common.response.ErrorResponse;
import com.concert.backend.common.response.ErrorResponse.ValidationError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tools.jackson.databind.exc.InvalidFormatException;

@Slf4j(topic = "CustomExceptionHandler")
@RestControllerAdvice
public class CustomExceptionHandler {

    private static final String PROBLEM_BASE_URI = "about:blank/";
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final Clock clock;

    public CustomExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleApp(
            AppException ex,
            HttpServletRequest request
    ) {
        ErrorCode code = ex.getErrorCode();

        log.warn(
                "비즈니스 예외. traceId={}, errorCode={}, method={}, uri={}",
                MDC.get(RequestTraceFilter.TRACE_ID),
                code.code(),
                request.getMethod(),
                request.getRequestURI()
        );

        return error(
                code,
                ex.getMessage(),
                request,
                ex.getParameters(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleInvalidBody(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        return appError(request, extractFieldErrors(ex));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleConstraint(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        return appError(request, extractFieldErrors(ex));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request
    ) {
        List<ValidationError> errors = List.of(
                ValidationError.of(
                        ex.getParameterName(),
                        "필수 요청 파라미터입니다."
                )
        );

        return appError(request, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        List<ValidationError> errors = List.of(
                ValidationError.of(
                        ex.getName(),
                        "요청 값의 타입이 올바르지 않습니다."
                )
        );

        return appError(request, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormat(
                    invalidFormatException,
                    request
            );
        }

        return appError(
                AppErrorCode.INVALID_JSON_FORMAT,
                request
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ) {
        return appError(AppErrorCode.METHOD_NOT_ALLOWED, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {
        return appError(AppErrorCode.ENTITY_NOT_FOUND, request);
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            AuthorizationDeniedException.class
    })
    public ResponseEntity<CommonResponse<ErrorResponse>> handleAccessDenied(
            Exception ex,
            HttpServletRequest request
    ) {
        return appError(AppErrorCode.ACCESS_DENIED, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleAuthentication(
            AuthenticationException ex,
            HttpServletRequest request
    ) {
        return appError(AppErrorCode.UNAUTHORIZED, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleAny(
            Exception ex,
            HttpServletRequest request
    ) {
        String traceId = MDC.get(RequestTraceFilter.TRACE_ID);

        log.error(
                "처리되지 않은 예외. traceId={}, method={}, uri={}",
                traceId,
                request.getMethod(),
                request.getRequestURI(),
                ex
        );

        return appError(
                AppErrorCode.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<CommonResponse<ErrorResponse>> handleInvalidFormat(
            InvalidFormatException ex,
            HttpServletRequest request
    ) {
        String field = ex.getPath()
                .stream()
                .map(reference -> {
                    if (reference.getPropertyName() != null) {
                        return reference.getPropertyName();
                    }

                    if (reference.getIndex() >= 0) {
                        return "[" + reference.getIndex() + "]";
                    }

                    return "requestBody";
                })
                .reduce(this::joinPath)
                .orElse("requestBody");

        Class<?> targetType = ex.getTargetType();

        if (targetType != null && targetType.isEnum()) {
            String allowedValues = java.util.Arrays.stream(
                            targetType.getEnumConstants()
                    )
                    .map(Object::toString)
                    .collect(java.util.stream.Collectors.joining(", "));

            List<ValidationError> errors = List.of(
                    ValidationError.of(
                            field,
                            "허용되는 값은 [" + allowedValues + "]입니다."
                    )
            );

            return error(
                    AppErrorCode.INVALID_ENUM_VALUE,
                    AppErrorCode.INVALID_ENUM_VALUE.message(),
                    request,
                    null,
                    errors
            );
        }

        List<ValidationError> errors = List.of(
                ValidationError.of(
                        field,
                        "요청 값의 형식이 올바르지 않습니다."
                )
        );

        return error(
                AppErrorCode.INVALID_INPUT_VALUE,
                AppErrorCode.INVALID_INPUT_VALUE.message(),
                request,
                null,
                errors
        );
    }

    private String joinPath(String left, String right) {
        if (right.startsWith("[")) {
            return left + right;
        }

        return left + "." + right;
    }

    private List<ValidationError> extractFieldErrors(
            MethodArgumentNotValidException ex
    ) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ValidationError.of(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .distinct()
                .sorted(java.util.Comparator.comparing(
                        ValidationError::field
                ))
                .toList();
    }


    private List<ValidationError> extractFieldErrors(
            ConstraintViolationException ex
    ) {
        return ex.getConstraintViolations()
                .stream()
                .map(violation -> ValidationError.of(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .distinct()
                .sorted(java.util.Comparator.comparing(
                        ValidationError::field
                ))
                .toList();
    }

    private String extractFieldName(String path) {
        int lastDotIndex = path.lastIndexOf('.');
        return lastDotIndex == -1
                ? path
                : path.substring(lastDotIndex + 1);
    }

    private ResponseEntity<CommonResponse<ErrorResponse>> appError(
            AppErrorCode code,
            HttpServletRequest request
    ) {
        return error(
                code,
                code.message(),
                request,
                null,
                null
        );
    }

    private ResponseEntity<CommonResponse<ErrorResponse>> appError(
            HttpServletRequest request,
            List<ValidationError> errors
    ) {
        AppErrorCode code = AppErrorCode.INVALID_INPUT_VALUE;

        return error(
                code,
                code.message(),
                request,
                null,
                errors
        );
    }

    private ResponseEntity<CommonResponse<ErrorResponse>> error(
            ErrorCode code,
            String detail,
            HttpServletRequest request,
            List<Object> parameters,
            List<ValidationError> errors
    ) {
        String errorCode = code.code();
        String traceId = MDC.get(RequestTraceFilter.TRACE_ID);

        ErrorResponse errorResponse = ErrorResponse.of(
                problemType(errorCode),
                errorCode,
                code.status(),
                detail,
                request.getRequestURI(),
                errorCode,
                traceId,
                parameters,
                errors
        );

        CommonResponse<ErrorResponse> commonResponse =
                CommonResponse.failure(
                        code.status().value(),
                        failureMessage(code.status().value()),
                        errorResponse,
                        now()
                );

        return ResponseEntity
                .status(code.status())
                .body(commonResponse);
    }

    private String problemType(String errorCode) {
        return PROBLEM_BASE_URI
                + errorCode.toLowerCase().replace('_', '-');
    }

    private String failureMessage(int status) {
        return switch (status) {
            case 400 -> "실패: 요청값이 올바르지 않습니다.";
            case 401 -> "실패: 인증이 필요합니다.";
            case 403 -> "실패: 요청 권한이 없습니다.";
            case 404 -> "실패: 요청한 리소스를 찾을 수 없습니다.";
            case 405 -> "실패: 지원하지 않는 요청 방식입니다.";
            case 409 -> "실패: 요청이 현재 상태와 충돌합니다.";
            case 500 -> "실패: 서버 내부 오류가 발생했습니다.";
            default -> "실패: 요청을 처리할 수 없습니다.";
        };
    }

    private LocalDateTime now() {
        return LocalDateTime.ofInstant(clock.instant(), SEOUL);
    }
}
