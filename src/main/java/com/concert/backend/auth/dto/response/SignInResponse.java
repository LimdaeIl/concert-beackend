package com.concert.backend.auth.dto.response;

public record SignInResponse(
        Long memberId,
        String accessToken,
        String refreshToken
) {

    public static SignInResponse from(SignInResult signInResult) {
        return new SignInResponse(
                signInResult.memberId(),
                signInResult.accessToken(),
                signInResult.refreshToken()
        );
    }
}
