package com.concert.backend.auth.presentation;

import com.concert.backend.auth.application.SignUpService;
import com.concert.backend.auth.dto.request.SignUpRequest;
import com.concert.backend.auth.dto.response.SignUpResponse;
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

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return signUpService.signUp(request);
    }
    

}
