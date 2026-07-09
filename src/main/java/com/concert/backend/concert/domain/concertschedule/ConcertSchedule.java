package com.concert.backend.concert.domain.concertschedule;

import com.concert.backend.concert.domain.concert.Concert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_concert_schedules")
@Entity
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @Column(name = "performance_at", nullable = false)
    private LocalDateTime performanceAt;

    @Column(name = "booking_start_at", nullable = false)
    private LocalDateTime bookingStartAt;

    @Column(name = "booking_end_at", nullable = false)
    private LocalDateTime bookingEndAt;

    @Column(name = "status", nullable = false)
    private ConcertScheduleStatus status;

}
