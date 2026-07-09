package com.concert.backend.member.dto.response;

import com.concert.backend.member.domain.Member;

public record GetMeResponse(
        Long id,
        String email,
        String name,
        String phone,
        String role,
        String status
) {

    public static GetMeResponse from(Member member) {
        return new GetMeResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhone(),
                member.getRole().name(),
                member.getStatus().name()
        );
    }
}
