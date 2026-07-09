package com.concert.backend.auth.dto.response;

public record ReissueResponse(
        Long memberId,
        String newAccessToken,
        String newRefreshToken
) {

    public static ReissueResponse of(Long memberId, String newAccessToken, String newRefreshToken) {
        return new  ReissueResponse(memberId, newAccessToken, newRefreshToken);
    }
}
