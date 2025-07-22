package com.safekids.backend.service;

import com.safekids.backend.domain.User;
import com.safekids.backend.domain.location.Location;
import com.safekids.backend.dto.location.LocationRequestDto;
import com.safekids.backend.dto.location.LocationResponseDto;
import com.safekids.backend.repository.LocationRepository;
import com.safekids.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public void saveLocation(String email, LocationRequestDto requestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Location location = Location.builder()
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .timestamp(LocalDateTime.now())
                .user(user)
                .build();

        locationRepository.save(location);
    }

    public LocationResponseDto getLatestLocation(String email, Long childId) {
        User guardian = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        User child = userRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("자녀를 찾을 수 없습니다."));

        // 자녀가 이 보호자의 자식인지 확인
        if (!child.getParent().getId().equals(guardian.getId())) {
            throw new SecurityException("해당 자녀에 접근할 권한이 없습니다.");
        }

        Location location = locationRepository.findTopByUserIdOrderByTimestampDesc(childId)
                .orElseThrow(() -> new IllegalArgumentException("위치 정보가 없습니다."));

        return new LocationResponseDto(location.getLatitude(), location.getLongitude(), location.getTimestamp());
    }

}
