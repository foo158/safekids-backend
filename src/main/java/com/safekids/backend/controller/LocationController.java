package com.safekids.backend.controller;

import com.safekids.backend.dto.location.LocationHistoryRequestDto;
import com.safekids.backend.dto.location.LocationRequestDto;
import com.safekids.backend.dto.location.LocationResponseDto;
import com.safekids.backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    //  위치 이력 조회 API 추가
    // ✅ 수정된 API: GET + @RequestParam 사용
    @GetMapping("/history")
    public ResponseEntity<List<LocationResponseDto>> getLocationHistory(
            @RequestParam Long childId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<LocationResponseDto> list = locationService.getLocationHistory(
                userDetails.getUsername(),
                childId,
                startDate,
                endDate
        );
        return ResponseEntity.ok(list);
    }


}
