package com.concert.backend.concert.dto.request;

import com.concert.backend.concert.domain.concert.ConcertCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateConcertRequest(

        @NotBlank(message = "콘서트 생성: 제목은 필수입니다.")
        @Size(max = 100, message = "콘서트 생성: 제목은 100자 이하여야 합니다.")
        String title,

        @NotBlank(message = "콘서트 생성: 설명은 필수입니다.")
        @Size(max = 5000, message = "콘서트 생성: 설명은 5,000자 이하여야 합니다.")
        String description,

        @NotBlank(message = "콘서트 생성: 주최자는 필수입니다.")
        @Size(max = 100, message = "콘서트 생성: 주최자는 100자 이하여야 합니다.")
        String organizer,

        @NotNull(message = "콘서트 생성: 카테고리는 필수입니다.")
        ConcertCategory category,

        @NotEmpty(message = "콘서트 생성: 공연 일정을 하나 이상 등록해야 합니다.")
        List<@Valid CreateConcertScheduleRequest> schedules,

        @NotEmpty(message = "콘서트 생성: 공연 이미지를 하나 이상 등록해야 합니다.")
        List<@Valid CreateConcertImageRequest> images
) {

}