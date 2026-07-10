package com.concert.backend.venue.infrastructure;

import com.concert.backend.venue.domain.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {

}
