package com.concert.backend.venue.application;

import com.concert.backend.venue.domain.Venue;
import com.concert.backend.venue.domain.VenueHall;
import com.concert.backend.venue.dto.request.CreateVenueHallRequest;
import com.concert.backend.venue.dto.request.CreateVenueRequest;
import com.concert.backend.venue.dto.response.CreateVenueResponse;
import com.concert.backend.venue.infrastructure.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateVenueService {

    private final VenueRepository venueRepository;

    public CreateVenueResponse create(CreateVenueRequest request) {

        Venue venue = Venue.create(
                request.venueName(),
                request.address(),
                request.detailAddress(),
                request.latitude(),
                request.longitude()
        );

        for (CreateVenueHallRequest hall : request.venueHalls()) {
            venue.addVenueHall(
                    VenueHall.create(
                            hall.hallName(),
                            hall.capacity()
                    )
            );
        }

        Venue savedVenue = venueRepository.save(venue);

        return CreateVenueResponse.from(savedVenue);
    }
}
