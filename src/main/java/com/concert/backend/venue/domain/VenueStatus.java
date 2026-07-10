package com.concert.backend.venue.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public enum VenueStatus {
    AVAILABLE("이용 가능"),
    UNAVAILABLE("이용 불가능");

    private final String description;
}
