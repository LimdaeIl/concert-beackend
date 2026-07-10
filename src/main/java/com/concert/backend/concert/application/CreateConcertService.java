package com.concert.backend.concert.application;

import com.concert.backend.concert.domain.concert.Concert;
import com.concert.backend.concert.domain.concert.ConcertImage;
import com.concert.backend.concert.domain.concertschedule.ConcertSchedule;
import com.concert.backend.concert.dto.request.CreateConcertImageRequest;
import com.concert.backend.concert.dto.request.CreateConcertRequest;
import com.concert.backend.concert.dto.request.CreateConcertScheduleRequest;
import com.concert.backend.concert.dto.response.CreateConcertResponse;
import com.concert.backend.concert.exception.ConcertErrorCode;
import com.concert.backend.concert.exception.ConcertException;
import com.concert.backend.concert.infrastructure.ConcertRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateConcertService {

    private final ConcertRepository concertRepository;

    public CreateConcertResponse create(CreateConcertRequest request) {
        validateRepresentativeImage(request);
        validateDuplicateImageSortOrder(request);
        validateDuplicateSchedules(request);

        Concert concert = Concert.create(
                request.title(),
                request.description(),
                request.organizer(),
                request.category()
        );

        addSchedules(concert, request);
        addImages(concert, request);

        Concert savedConcert = concertRepository.save(concert);

        return CreateConcertResponse.from(savedConcert);
    }

    private void addSchedules(
            Concert concert,
            CreateConcertRequest request
    ) {
        for (CreateConcertScheduleRequest scheduleRequest
                : request.schedules()) {

            ConcertSchedule schedule = ConcertSchedule.create(
                    scheduleRequest.venueHallId(),
                    scheduleRequest.performanceAt(),
                    scheduleRequest.bookingStartAt(),
                    scheduleRequest.bookingEndAt()
            );

            concert.addSchedule(schedule);
        }
    }

    private void addImages(
            Concert concert,
            CreateConcertRequest request
    ) {
        for (CreateConcertImageRequest imageRequest : request.images()) {
            ConcertImage image = ConcertImage.create(
                    imageRequest.imageUrl(),
                    imageRequest.sortOrder(),
                    imageRequest.representative()
            );

            concert.addImage(image);
        }
    }

    private void validateRepresentativeImage(
            CreateConcertRequest request
    ) {
        long representativeCount = request.images()
                .stream()
                .filter(CreateConcertImageRequest::representative)
                .count();

        if (representativeCount != 1) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_REPRESENTATIVE_IMAGE_COUNT
            );
        }
    }

    private void validateDuplicateImageSortOrder(
            CreateConcertRequest request
    ) {
        Integer duplicateSortOrder = request.images()
                .stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                CreateConcertImageRequest::sortOrder,
                                java.util.stream.Collectors.counting()
                        )
                )
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(java.util.Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (duplicateSortOrder != null) {
            throw new ConcertException(
                    ConcertErrorCode.DUPLICATE_IMAGE_SORT_ORDER,
                    duplicateSortOrder
            );
        }
    }

    private void validateDuplicateSchedules(
            CreateConcertRequest request
    ) {
        Map<ScheduleKey, Long> scheduleCountMap = request.schedules()
                .stream()
                .collect(Collectors.groupingBy(
                        schedule -> new ScheduleKey(
                                schedule.venueHallId(),
                                schedule.performanceAt()
                        ),
                        Collectors.counting()
                ));

        ScheduleKey duplicateScheduleKey = scheduleCountMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (duplicateScheduleKey != null) {
            throw new ConcertException(
                    ConcertErrorCode.DUPLICATE_CONCERT_SCHEDULE,
                    duplicateScheduleKey.venueHallId(),
                    duplicateScheduleKey.performanceAt()
            );
        }
    }

    private record ScheduleKey(
            Long venueHallId,
            LocalDateTime performanceAt
    ) {
    }
}
