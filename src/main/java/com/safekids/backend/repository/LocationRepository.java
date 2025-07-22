package com.safekids.backend.repository;

import com.safekids.backend.domain.User;
import com.safekids.backend.domain.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    // 최신 위치 1개 조회
    Optional<Location> findTopByUserIdOrderByTimestampDesc(Long userId);

    //  위치 이력 조회 (필터: user + 시간 범위)
    List<Location> findByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);
}
