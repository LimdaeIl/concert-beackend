package com.concert.backend.auth.presentation;

import com.concert.backend.auth.application.SignUpService;
import com.concert.backend.auth.dto.request.SignUpRequest;
import com.concert.backend.auth.dto.response.SignUpResponse;
import com.concert.backend.common.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final SignUpService signUpService;

    @PostMapping("/sign-up")
    public CommonResponse<SignUpResponse> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        SignUpResponse response = signUpService.signUp(request);

        return CommonResponse.created(
                "회원가입: 회원가입에 성공했습니다.",
                response);
    }

}
