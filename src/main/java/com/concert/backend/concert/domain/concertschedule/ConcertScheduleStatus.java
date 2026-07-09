package com.concert.backend.concert.domain.concertschedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ConcertScheduleStatus {
    UPCOMING("UPCOMING", "공연 예정"),
    OPEN("OPEN", "공연 진행 중"),
    SOLD_OUT("SOLD_OUT", "공연 매진"),
    CLOSE("CLOSE", "공연 종료"),
    CANCELLED("CANCELLED", "공연 취소");

    private final String code;
    private final String description;
}
