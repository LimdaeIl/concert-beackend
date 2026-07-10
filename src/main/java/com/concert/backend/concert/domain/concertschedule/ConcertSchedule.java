package com.concert.backend.concert.domain.concertschedule;

import com.concert.backend.common.audit.BaseAuditEntity;
import com.concert.backend.concert.domain.concert.Concert;
import com.concert.backend.concert.exception.ConcertErrorCode;
import com.concert.backend.concert.exception.ConcertException;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_concert_schedules")
@Entity
public class ConcertSchedule extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "venue_hall_id", nullable = false)
    private Long venueHallId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @Column(name = "performance_at", nullable = false)
    private LocalDateTime performanceAt;

    @Column(name = "booking_start_at", nullable = false)
    private LocalDateTime bookingStartAt;

    @Column(name = "booking_end_at", nullable = false)
    private LocalDateTime bookingEndAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ConcertScheduleStatus status;

    private ConcertSchedule(
            Long venueHallId,
            LocalDateTime performanceAt,
            LocalDateTime bookingStartAt,
            LocalDateTime bookingEndAt
    ) {
        validate(
                venueHallId,
                performanceAt,
                bookingStartAt,
                bookingEndAt
        );

        this.venueHallId = venueHallId;
        this.performanceAt = performanceAt;
        this.bookingStartAt = bookingStartAt;
        this.bookingEndAt = bookingEndAt;
        this.status = ConcertScheduleStatus.UPCOMING;
    }

    public static ConcertSchedule create(
            Long venueHallId,
            LocalDateTime performanceAt,
            LocalDateTime bookingStartAt,
            LocalDateTime bookingEndAt
    ) {
        return new ConcertSchedule(
                venueHallId,
                performanceAt,
                bookingStartAt,
                bookingEndAt
        );
    }

    public void assignConcert(Concert concert) {
        this.concert = concert;
    }

    private void validate(
            Long venueHallId,
            LocalDateTime performanceAt,
            LocalDateTime bookingStartAt,
            LocalDateTime bookingEndAt
    ) {
        if (venueHallId == null || venueHallId <= 0) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_VENUE_HALL_ID,
                    venueHallId
            );
        }

        if (performanceAt == null
                || bookingStartAt == null
                || bookingEndAt == null) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_SCHEDULE_TIME
            );
        }

        if (!bookingStartAt.isBefore(bookingEndAt)
                || !bookingEndAt.isBefore(performanceAt)) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_SCHEDULE_TIME
            );
        }
    }
}
