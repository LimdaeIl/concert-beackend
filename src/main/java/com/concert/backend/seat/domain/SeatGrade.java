package com.concert.backend.seat.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SeatGrade {
    VIP("VIP", "Very Important Person 첫 번째 좋은 좌석"),
    R("R", "Royal 두 번째 좋은 좌석"),
    S("S", "Superior 세 번째 좋은 좌석"),
    A("A", "A grade 네 번째 좋은 좌석");

    private final String code;
    private final String description;
}
