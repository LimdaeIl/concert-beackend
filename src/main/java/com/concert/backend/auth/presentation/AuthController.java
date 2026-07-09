package com.concert.backend.auth.presentation;

import com.concert.backend.auth.application.RefreshTokenCookieProvider;
import com.concert.backend.auth.application.SignInService;
import com.concert.backend.auth.application.SignUpService;
import com.concert.backend.auth.dto.request.SignInRequest;
import com.concert.backend.auth.dto.request.SignUpRequest;
import com.concert.backend.auth.dto.response.SignInResponse;
import com.concert.backend.auth.dto.response.SignInResult;
import com.concert.backend.auth.dto.response.SignUpResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final SignUpService signUpService;
    private final SignInService signInService;
    private final RefreshTokenCookieProvider cookieProvider;


    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return signUpService.signUp(request);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public SignInResponse signIn(
            @Valid @RequestBody SignInRequest request,
            HttpServletResponse servletResponse
    ) {
        SignInResult signInResult = signInService.signIn(request);

        cookieProvider.addRefreshTokenCookie(
                servletResponse,
                signInResult.refreshToken(),
                signInResult.refreshTokenRemainingSecond()
        );

        return SignInResponse.from(signInResult);
    }


}
