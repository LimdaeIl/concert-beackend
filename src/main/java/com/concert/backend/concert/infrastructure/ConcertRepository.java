package com.concert.backend.concert.infrastructure;

import com.concert.backend.concert.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

}
