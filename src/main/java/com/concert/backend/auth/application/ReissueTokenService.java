package com.concert.backend.auth.application;

import com.concert.backend.auth.dto.response.ReissueTokenResult;
import com.concert.backend.auth.exception.AuthErrorCode;
import com.concert.backend.auth.exception.AuthException;
import com.concert.backend.auth.infrastructure.jwt.JWTHashUtil;
import com.concert.backend.auth.infrastructure.jwt.JwtProvider;
import com.concert.backend.auth.infrastructure.redis.RefreshTokenRepository;
import com.concert.backend.member.domain.Member;
import com.concert.backend.member.infrastructure.MemberRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReissueTokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTHashUtil jwtHashUtil;
    private final JwtProvider jwtProvider;

    public ReissueTokenResult reissue(String refreshToken) {
        validateRefreshToken(refreshToken);

        Long memberId = jwtProvider.getMemberIdFromRefreshToken(refreshToken);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.NOT_FOUND_MEMBER));

        String newAccessToken = jwtProvider.createAccessToken(
                member.getId(),
                member.getRole().name()
        );
        String newRefreshToken = jwtProvider.createRefreshToken(member.getId());

        String hashedOldRefreshToken = jwtHashUtil.sha256(refreshToken);
        String hashedNewRefreshToken = jwtHashUtil.sha256(newRefreshToken);

        Duration refreshTokenTtl =
                Duration.ofMillis(jwtProvider.getRefreshTokenExpirationMillis());

        refreshTokenRepository.rotateIfMatches(
                member.getId(),
                hashedOldRefreshToken,
                hashedNewRefreshToken,
                refreshTokenTtl
        );

        return ReissueTokenResult.of(
                member.getId(),
                newAccessToken,
                newRefreshToken,
                refreshTokenTtl.toSeconds()
        );
    }

    private void validateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.MISSING_REFRESH_TOKEN);
        }
    }
}

