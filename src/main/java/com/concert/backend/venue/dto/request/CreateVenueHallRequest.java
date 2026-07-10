package com.concert.backend.venue.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateVenueHallRequest(

        @NotBlank(message = "공연장 생성: 홀 이름은 필수입니다.")
        @Size(max = 100, message = "공연장 생성: 홀 이름은 100자 이하여야 합니다.")
        String hallName,

        @NotNull(message = "공연장 생성: 홀 수용 인원은 필수입니다.")
        @Min(value = 1, message = "공연장 생성: 홀 수용 인원은 1명 이상이어야 합니다.")
        @Max(value = 100000, message = "공연장 생성: 홀 수용 인원은 100,000명 이하여야 합니다.")
        Integer capacity
) {

}
