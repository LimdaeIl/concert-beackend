package com.concert.backend.common.response;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final Clock clock;

    public CommonResponseAdvice(Clock clock) {
        this.clock = clock;
    }

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        Class<?> controllerType = returnType.getContainingClass();

        Package declaringPackage = controllerType.getPackage();

        if (declaringPackage == null
                || !declaringPackage.getName()
                .startsWith("com.concert.backend")) {
            return false;
        }

        boolean methodSkipped = returnType.hasMethodAnnotation(
                SkipCommonResponse.class
        );

        boolean classSkipped = controllerType.isAnnotationPresent(
                SkipCommonResponse.class
        );

        return !methodSkipped && !classSkipped;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        if (body instanceof CommonResponse<?>) {
            return body;
        }

        if (body instanceof String) {
            return body;
        }

        if (!isJson(selectedContentType)) {
            return body;
        }

        int status = getStatus(response);

        if (status == 204) {
            return null;
        }

        if (status >= 400) {
            return body;
        }

        return CommonResponse.success(
                status,
                successMessage(status),
                body,
                now()
        );
    }

    private boolean isJson(MediaType mediaType) {
        return MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)
                || mediaType.getSubtype().endsWith("+json");
    }

    private int getStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse servletResponse) {
            return servletResponse.getServletResponse().getStatus();
        }

        return 200;
    }

    private String successMessage(int status) {
        return switch (status) {
            case 201 -> "성공: 리소스가 생성되었습니다.";
            case 204 -> "성공: 요청이 성공적으로 처리되었습니다.";
            default -> "성공: 요청이 성공적으로 처리되었습니다.";
        };
    }

    private LocalDateTime now() {
        return LocalDateTime.ofInstant(clock.instant(), SEOUL);
    }
}
