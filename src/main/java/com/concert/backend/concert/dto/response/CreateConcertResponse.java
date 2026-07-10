package com.concert.backend.concert.dto.response;

import com.concert.backend.concert.domain.concert.Concert;
import java.util.List;

public record CreateConcertResponse(

        Long concertId,

        String title,

        String description,

        String organizer,

        String status,

        String category,

        List<CreateConcertScheduleResponse> schedules,

        List<CreateConcertImageResponse> images
) {

    public static CreateConcertResponse from(Concert concert) {
        return new CreateConcertResponse(
                concert.getId(),
                concert.getTitle(),
                concert.getDescription(),
                concert.getOrganizer(),
                concert.getStatus().name(),
                concert.getCategory().name(),
                concert.getSchedules()
                        .stream()
                        .map(CreateConcertScheduleResponse::from)
                        .toList(),
                concert.getImages()
                        .stream()
                        .map(CreateConcertImageResponse::from)
                        .toList()
        );
    }
}
