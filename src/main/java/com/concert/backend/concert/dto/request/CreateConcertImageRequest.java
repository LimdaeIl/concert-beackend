package com.concert.backend.concert.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateConcertImageRequest(

        @NotBlank(message = "콘서트 생성: 이미지 URL은 필수입니다.")
        @Size(max = 500, message = "콘서트 생성: 이미지 URL은 500자 이하여야 합니다.")
        String imageUrl,

        @Min(value = 0, message = "콘서트 생성: 이미지 정렬 순서는 0 이상이어야 합니다.")
        @Max(value = 100, message = "콘서트 생성: 이미지 정렬 순서는 100 이하여야 합니다.")
        int sortOrder,

        boolean representative
) {

}
