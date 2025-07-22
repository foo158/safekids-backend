package com.safekids.backend.repository;

import com.safekids.backend.domain.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findTopByUserIdOrderByTimestampDesc(Long userId);
}
