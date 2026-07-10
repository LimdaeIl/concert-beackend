package com.concert.backend.venue.domain;

import com.concert.backend.common.audit.BaseAuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_venues")
@Entity
public class Venue extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "venue_name", nullable = false, length = 100)
    private String venueName;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "detail_address", length = 100)
    private String detailAddress;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @OneToMany(
            mappedBy = "venue",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<VenueHall> venueHalls = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "venue_status", nullable = false, length = 20)
    private VenueStatus venueStatus;

    private Venue(
            String venueName,
            String address,
            String detailAddress,
            Double latitude,
            Double longitude
    ) {
        this.venueName = venueName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.venueStatus = VenueStatus.AVAILABLE;
    }

    public static Venue create(
            String venueName,
            String address,
            String detailAddress,
            Double latitude,
            Double longitude
    ) {
        return new Venue(
                venueName,
                address,
                detailAddress,
                latitude,
                longitude
        );
    }

    public void addVenueHall(VenueHall venueHall) {
        if (venueHall == null) {
            throw new IllegalArgumentException("공연장 홀은 필수입니다.");
        }

        venueHalls.add(venueHall);
        venueHall.assignVenue(this);
    }

    public List<VenueHall> getVenueHalls() {
        return Collections.unmodifiableList(venueHalls);
    }
}
