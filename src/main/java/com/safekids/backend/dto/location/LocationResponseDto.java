package com.safekids.backend.dto.location;

import com.safekids.backend.domain.location.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LocationResponseDto {

    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;

    // ✅ Location 엔티티 → LocationResponseDto 변환
    @SuppressWarnings("all")  // Lombok getter 경고 무시
    public static LocationResponseDto fromEntity(Location location) {
        return new LocationResponseDto(
                location.getLatitude(),
                location.getLongitude(),
                location.getTimestamp()
        );
    }
}
