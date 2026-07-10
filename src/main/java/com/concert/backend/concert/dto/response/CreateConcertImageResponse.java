package com.concert.backend.concert.dto.response;

import com.concert.backend.concert.domain.concert.ConcertImage;

public record CreateConcertImageResponse(

        Long concertImageId,

        String imageUrl,

        int sortOrder,

        boolean representative
) {

    public static CreateConcertImageResponse from(ConcertImage image) {
        return new CreateConcertImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.getSortOrder(),
                image.isRepresentative()
        );
    }
}
