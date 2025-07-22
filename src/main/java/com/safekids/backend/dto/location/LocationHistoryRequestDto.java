package com.safekids.backend.dto.location;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class LocationHistoryRequestDto {
    private Long childId;
    private LocalDateTime startDate;  // 선택
    private LocalDateTime endDate;    // 선택
}