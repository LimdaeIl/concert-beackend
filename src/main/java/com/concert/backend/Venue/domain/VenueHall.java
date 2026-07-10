package com.concert.backend.venue.domain;

import com.concert.backend.common.audit.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_venue_halls")
@Entity
public class VenueHall extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(name = "hall_name", nullable = false, length = 100)
    private String hallName;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "venue_hall_status", nullable = false, length = 20)
    private VenueHallStatus venueHallStatus;

    private VenueHall(
            String hallName,
            Integer capacity
    ) {
        this.hallName = hallName;
        this.capacity = capacity;
        this.venueHallStatus = VenueHallStatus.AVAILABLE;
    }

    public static VenueHall create(
            String hallName,
            Integer capacity
    ) {
        return new VenueHall(hallName, capacity);
    }

    void assignVenue(Venue venue) {
        this.venue = venue;
    }
}
