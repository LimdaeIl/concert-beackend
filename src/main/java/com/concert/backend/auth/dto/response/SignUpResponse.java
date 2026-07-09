package com.concert.backend.auth.dto.response;

import com.concert.backend.member.domain.Member;

public record SignUpResponse(
        Long id,
        String email,
        String name,
        String phone
) {

    public static SignUpResponse from(Member member) {
            return new SignUpResponse(
                    member.getId(),
                    member.getEmail(),
                    member.getName(),
                    member.getPhone()
            );
    }
}
