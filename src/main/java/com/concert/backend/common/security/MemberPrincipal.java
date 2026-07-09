package com.concert.backend.common.security;

public record MemberPrincipal(
        Long memberId,
        String role
) {

}
