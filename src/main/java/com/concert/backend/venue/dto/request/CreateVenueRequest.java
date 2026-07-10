package com.concert.backend.venue.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateVenueRequest(

        @NotBlank(message = "공연장 생성: 공연장 이름은 필수입니다.")
        @Size(max = 100, message = "공연장 생성: 공연장 이름은 100자 이하여야 합니다.")
        String venueName,

        @NotBlank(message = "공연장 생성: 주소는 필수입니다.")
        @Size(max = 200, message = "공연장 생성: 주소는 200자 이하여야 합니다.")
        String address,

        @Size(max = 100, message = "공연장 생성: 상세 주소는 100자 이하여야 합니다.")
        String detailAddress,

        @NotNull(message = "공연장 생성: 위도는 필수입니다.")
        @DecimalMin(value = "-90.0", message = "공연장 생성: 위도는 -90 이상이어야 합니다.")
        @DecimalMax(value = "90.0", message = "공연장 생성: 위도는 90 이하여야 합니다.")
        Double latitude,

        @NotNull(message = "공연장 생성: 경도는 필수입니다.")
        @DecimalMin(value = "-180.0", message = "공연장 생성: 경도는 -180 이상이어야 합니다.")
        @DecimalMax(value = "180.0", message = "공연장 생성: 경도는 180 이하여야 합니다.")
        Double longitude,

        @NotEmpty(message = "공연장 생성: 공연장 홀을 하나 이상 등록해야 합니다.")
        List<@Valid CreateVenueHallRequest> venueHalls
) {

}
