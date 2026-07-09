package com.concert.backend.seatinventory.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SeatInventoryStatus {
    AVAILABLE("AVAILABLE", "예매 가능"),
    HOLD("HOLD", "선점/결제 중"),
    SOLD_OUT("SOLD_OUT", "판매 완료"),
    BLOCKED("BLOCKED", "판매 불가");

    private final String code;
    private final String description;

}
