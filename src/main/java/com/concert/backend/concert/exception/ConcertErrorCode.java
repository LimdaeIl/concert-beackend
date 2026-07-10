package com.concert.backend.concert.exception;

import com.concert.backend.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ConcertErrorCode implements ErrorCode {

    NOT_FOUND_CONCERT(HttpStatus.NOT_FOUND, "concert: Concert ID '%s' not found."),
    INVALID_TITLE(HttpStatus.BAD_REQUEST, "concert: Concert title is invalid."),
    INVALID_DESCRIPTION(HttpStatus.BAD_REQUEST, "concert: Concert description is invalid."),
    INVALID_ORGANIZER(HttpStatus.BAD_REQUEST, "concert: Concert organizer is invalid."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "concert: Concert category is invalid."),
    EMPTY_SCHEDULES(HttpStatus.BAD_REQUEST, "concert: At least one concert schedule is required."),
    INVALID_SCHEDULE_TIME(HttpStatus.BAD_REQUEST,
            "concert: Booking start time must be before booking end time, and booking end time must be before performance time."),
    INVALID_VENUE_HALL_ID(HttpStatus.BAD_REQUEST, "concert: Venue hall ID '%s' is invalid."),
    NOT_FOUND_VENUE_HALL(HttpStatus.NOT_FOUND, "concert: Venue hall ID '%s' not found."),
    UNAVAILABLE_VENUE_HALL(HttpStatus.CONFLICT, "concert: Venue hall ID '%s' is unavailable."),
    EMPTY_IMAGES(HttpStatus.BAD_REQUEST, "concert: At least one concert image is required."),
    INVALID_IMAGE_URL(HttpStatus.BAD_REQUEST, "concert: Concert image URL is invalid."),
    INVALID_IMAGE_SORT_ORDER(HttpStatus.BAD_REQUEST,
            "concert: Concert image sort order '%s' is invalid."),
    INVALID_REPRESENTATIVE_IMAGE_COUNT(HttpStatus.BAD_REQUEST,
            "concert: Exactly one representative image is required."),
    NULL_CONCERT_SCHEDULE(HttpStatus.BAD_REQUEST, "concert: Concert schedule must not be null."),
    DUPLICATE_IMAGE_SORT_ORDER(HttpStatus.CONFLICT, "concert: Duplicate image sort order '%s'."),
    NULL_CONCERT_IMAGE(HttpStatus.BAD_REQUEST, "concert: Concert image must not be null."),
    INVALID_CONCERT_REFERENCE(HttpStatus.BAD_REQUEST, "concert: Concert reference must not be null."),
    DUPLICATE_CONCERT_SCHEDULE(HttpStatus.CONFLICT, "concert: A schedule already exists for venue hall ID '%s' at '%s'."),;

    private final HttpStatus httpStatus;
    private final String messageTemplate;

    @Override
    public HttpStatus status() {
        return httpStatus;
    }

    @Override
    public String message() {
        return messageTemplate;
    }
}
