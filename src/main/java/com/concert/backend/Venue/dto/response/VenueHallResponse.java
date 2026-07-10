package com.concert.backend.venue.dto.response;

import com.concert.backend.venue.domain.VenueHall;

public record VenueHallResponse(

        Long venueHallId,

        String hallName,

        Integer capacity,

        String venueHallStatus
) {

    public static VenueHallResponse from(VenueHall venueHall) {
        return new VenueHallResponse(
                venueHall.getId(),
                venueHall.getHallName(),
                venueHall.getCapacity(),
                venueHall.getVenueHallStatus().name()
        );
    }
}
