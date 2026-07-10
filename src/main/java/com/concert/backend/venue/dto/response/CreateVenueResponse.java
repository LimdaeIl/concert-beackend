package com.concert.backend.venue.dto.response;

import com.concert.backend.venue.domain.Venue;
import java.util.List;

public record CreateVenueResponse(

        Long venueId,

        String venueName,

        String address,

        String detailAddress,

        Double latitude,

        Double longitude,

        String venueStatus,

        List<VenueHallResponse> venueHalls
) {

    public static CreateVenueResponse from(Venue venue) {
        return new CreateVenueResponse(
                venue.getId(),
                venue.getVenueName(),
                venue.getAddress(),
                venue.getDetailAddress(),
                venue.getLatitude(),
                venue.getLongitude(),
                venue.getVenueStatus().name(),
                venue.getVenueHalls()
                        .stream()
                        .map(VenueHallResponse::from)
                        .toList()
        );
    }
}