package com.concert.backend.auth.application;

import com.concert.backend.auth.domain.Credential;
import com.concert.backend.auth.dto.request.SignUpRequest;
import com.concert.backend.auth.dto.response.SignUpResponse;
import com.concert.backend.auth.infrastructure.CredentialRepository;
import com.concert.backend.member.domain.Member;
import com.concert.backend.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SignUpService {

    private final CredentialRepository credentialRepository;
    private final MemberRepository memberRepository;

    public SignUpResponse signUp(SignUpRequest request) {

        if (memberRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("signUp: already exists By Email");
        }

        if (memberRepository.existsByPhone(request.phone())) {
            throw new IllegalArgumentException("signUp: already exists By Phone");
        }

        Member member = Member.create(
                request.email(),
                request.name(),
                request.phone()
        );

        memberRepository.save(member);

        Credential credential = Credential.create(member.getId(), request.password());

        credentialRepository.save(credential);

        return SignUpResponse.from(member);
    }
}
