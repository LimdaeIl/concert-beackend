package com.concert.backend.member.application;

import com.concert.backend.common.security.MemberPrincipal;
import com.concert.backend.member.domain.Member;
import com.concert.backend.member.domain.MemberStatus;
import com.concert.backend.member.dto.response.GetMeResponse;
import com.concert.backend.member.exception.MemberErrorCode;
import com.concert.backend.member.exception.MemberException;
import com.concert.backend.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GetMeService {

    private final MemberRepository memberRepository;

    public GetMeResponse me(MemberPrincipal principal) {
        Member member = memberRepository.findById(principal.memberId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER,
                        principal.memberId()));

        if (member.getStatus().equals(MemberStatus.DELETED)) {
            throw new MemberException(MemberErrorCode.DELETED_MEMBER, principal.memberId());
        }

        if (member.getStatus().equals(MemberStatus.DEACTIVATE)) {
            throw new MemberException(MemberErrorCode.DEACTIVATE_MEMBER, principal.memberId());
        }

        return GetMeResponse.from(member);
    }
}

