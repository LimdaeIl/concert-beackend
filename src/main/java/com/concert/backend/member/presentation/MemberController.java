package com.concert.backend.member.presentation;

import com.concert.backend.common.security.MemberPrincipal;
import com.concert.backend.member.application.GetMeService;
import com.concert.backend.member.dto.response.GetMeResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController {

    private final GetMeService getMeService;

    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    @GetMapping
    public GetMeResponse me(
            @AuthenticationPrincipal MemberPrincipal principal
    ) {
        return getMeService.me(principal);

    }
}
