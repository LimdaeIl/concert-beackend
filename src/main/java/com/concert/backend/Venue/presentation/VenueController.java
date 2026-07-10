package com.concert.backend.venue.presentation;


import com.concert.backend.venue.application.CreateVenueService;
import com.concert.backend.venue.dto.request.CreateVenueRequest;
import com.concert.backend.venue.dto.response.CreateVenueResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/venue-halls")
@RestController
public class VenueController {

    private final CreateVenueService createVenueService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CreateVenueResponse create(
            @Valid @RequestBody CreateVenueRequest request
    ) {
        return createVenueService.create(request);
    }
}
