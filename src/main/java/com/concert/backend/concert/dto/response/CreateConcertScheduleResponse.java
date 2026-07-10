package com.concert.backend.concert.dto.response;

import com.concert.backend.concert.domain.concertschedule.ConcertSchedule;
import java.time.LocalDateTime;

public record CreateConcertScheduleResponse(

        Long concertScheduleId,

        Long venueHallId,

        LocalDateTime performanceAt,

        LocalDateTime bookingStartAt,

        LocalDateTime bookingEndAt,

        String status
) {

    public static CreateConcertScheduleResponse from(
            ConcertSchedule schedule
    ) {
        return new CreateConcertScheduleResponse(
                schedule.getId(),
                schedule.getVenueHallId(),
                schedule.getPerformanceAt(),
                schedule.getBookingStartAt(),
                schedule.getBookingEndAt(),
                schedule.getStatus().name()
        );
    }
}
