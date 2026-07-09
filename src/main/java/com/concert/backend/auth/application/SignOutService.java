package com.concert.backend.auth.application;

import com.concert.backend.auth.exception.AuthErrorCode;
import com.concert.backend.auth.exception.AuthException;
import com.concert.backend.auth.infrastructure.jwt.JWTHashUtil;
import com.concert.backend.auth.infrastructure.jwt.JwtProvider;
import com.concert.backend.auth.infrastructure.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SignOutService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final JWTHashUtil jwtHashUtil;

    public void signOut(String refreshToken) {
        validateRefreshToken(refreshToken);

        Long memberId = jwtProvider.getMemberIdFromRefreshToken(refreshToken);
        String hashedRefreshToken = jwtHashUtil.sha256(refreshToken);

        String storedRefreshTokenHash = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.MISSING_REFRESH_TOKEN));

        if (!hashedRefreshToken.equals(storedRefreshTokenHash)) {
            throw new AuthException(AuthErrorCode.MISMATCH_REFRESH_TOKEN);
        }

        refreshTokenRepository.deleteByMemberId(memberId);
    }

    public void signOutByMemberId(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }

    private void validateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.MISSING_REFRESH_TOKEN);
        }
    }
}
