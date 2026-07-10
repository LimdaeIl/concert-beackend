package com.concert.backend.concert.presentation;

import com.concert.backend.concert.application.CreateConcertService;
import com.concert.backend.concert.dto.request.CreateConcertRequest;
import com.concert.backend.concert.dto.response.CreateConcertResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
@RestController
public class ConcertController {

    private final CreateConcertService createConcertService;

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreateConcertResponse create(
            @Valid @RequestBody CreateConcertRequest request
    ) {
        return createConcertService.create(request);
    }
}
