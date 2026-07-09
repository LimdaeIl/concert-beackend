package com.concert.backend.auth.application;

import com.concert.backend.auth.domain.Credential;
import com.concert.backend.auth.dto.request.SignInRequest;
import com.concert.backend.auth.dto.response.SignInResult;
import com.concert.backend.auth.exception.AuthErrorCode;
import com.concert.backend.auth.exception.AuthException;
import com.concert.backend.auth.infrastructure.jpa.CredentialRepository;
import com.concert.backend.auth.infrastructure.jwt.JWTHashUtil;
import com.concert.backend.auth.infrastructure.jwt.JwtProvider;
import com.concert.backend.auth.infrastructure.redis.RefreshTokenRepository;
import com.concert.backend.member.domain.Member;
import com.concert.backend.member.infrastructure.MemberRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SignInService {

    private final MemberRepository memberRepository;
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTHashUtil jwtHashUtil;
    private final JwtProvider jwtProvider;

    public SignInResult signIn(SignInRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(
                        () -> new AuthException(AuthErrorCode.NOT_FOUND_MEMBER, request.email()));

        Credential credential = credentialRepository.findByMemberId(member.getId())
                .orElseThrow(
                        () -> new AuthException(AuthErrorCode.NOT_FOUND_MEMBER, member.getId()));

        if (!passwordEncoder.matches(request.password(), credential.getPassword())) {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.createAccessToken(member.getId(), member.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(member.getId());


        String hashedRefreshToken = jwtHashUtil.sha256(refreshToken);
        long refreshTokenRemainingMillis = jwtProvider.getRefreshTokenRemainingMillis(refreshToken);

        refreshTokenRepository.save(
                member.getId(),
                hashedRefreshToken,
                Duration.ofMillis(refreshTokenRemainingMillis)
        );

        return SignInResult.of(
                member.getId(),
                accessToken,
                refreshToken,
                jwtProvider.getRefreshTokenRemainingSeconds(refreshToken)
        );
    }
}
