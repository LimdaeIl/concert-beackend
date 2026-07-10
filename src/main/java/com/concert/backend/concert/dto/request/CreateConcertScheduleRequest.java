package com.concert.backend.concert.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record CreateConcertScheduleRequest(

        @NotNull(message = "콘서트 생성: 공연 홀 ID는 필수입니다.")
        @Positive(message = "콘서트 생성: 공연 홀 ID는 양수여야 합니다.")
        Long venueHallId,

        @NotNull(message = "콘서트 생성: 공연 일시는 필수입니다.")
        @Future(message = "콘서트 생성: 공연 일시는 현재 이후여야 합니다.")
        LocalDateTime performanceAt,

        @NotNull(message = "콘서트 생성: 예매 시작 일시는 필수입니다.")
        @Future(message = "콘서트 생성: 예매 시작 일시는 현재 이후여야 합니다.")
        LocalDateTime bookingStartAt,

        @NotNull(message = "콘서트 생성: 예매 종료 일시는 필수입니다.")
        @Future(message = "콘서트 생성: 예매 종료 일시는 현재 이후여야 합니다.")
        LocalDateTime bookingEndAt
) {

}
