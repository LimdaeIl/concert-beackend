package com.concert.backend.venue.infrastructure;

import com.concert.backend.venue.domain.VenueHall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueHallRepository extends JpaRepository<VenueHall, Long> {

}
