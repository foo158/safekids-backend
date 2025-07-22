package com.safekids.backend.controller;

import com.safekids.backend.dto.location.LocationRequestDto;
import com.safekids.backend.dto.location.LocationResponseDto;
import com.safekids.backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<Void> saveLocation(@RequestBody LocationRequestDto requestDto,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        locationService.saveLocation(userDetails.getUsername(), requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/latest")
    public ResponseEntity<LocationResponseDto> getLatestLocation(@RequestParam Long childId,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        LocationResponseDto responseDto = locationService.getLatestLocation(userDetails.getUsername(), childId);
        return ResponseEntity.ok(responseDto);
    }

}
