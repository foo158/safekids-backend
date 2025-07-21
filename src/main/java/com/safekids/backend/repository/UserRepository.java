package com.safekids.backend.repository;

import com.safekids.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    // 부모 이메일로 자녀 목록 조회 (역방향 매핑)
    List<User> findByParentEmail(String parentEmail);

    // 부모 ID로 자녀 조회
    List<User> findByParent(User parent);


}
