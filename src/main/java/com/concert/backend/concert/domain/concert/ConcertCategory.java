package com.concert.backend.concert.domain.concert;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ConcertCategory {
    DANCE("DANCE", "댄스"),
    ROCK("ROCK", "락"),
    HIPHOP("HIPHOP", "힙합"),
    IDOL("IDOL", "아이돌"),
    BAND("BAND", "밴드"),
    BALLADE("BALLADE", "발라드");

    private final String code;
    private final String description;
}
