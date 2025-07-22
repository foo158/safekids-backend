package com.safekids.backend.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LocationResponseDto {
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
}
