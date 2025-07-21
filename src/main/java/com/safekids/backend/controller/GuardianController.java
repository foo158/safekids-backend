package com.safekids.backend.controller;

import com.safekids.backend.domain.User;
import com.safekids.backend.dto.ChildRegisterRequestDto;
import com.safekids.backend.dto.ChildResponse;
import com.safekids.backend.exception.CustomException;
import com.safekids.backend.repository.UserRepository;
import com.safekids.backend.service.UserService;
import com.safekids.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/guardian")
@RequiredArgsConstructor
public class GuardianController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    //  JWT 토큰에서 이메일 추출하는 유틸
    private String extractEmail(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            return jwtUtil.extractEmail(token);
        }
        throw new IllegalArgumentException("Authorization 헤더가 유효하지 않습니다.");
    }

    //  자녀 등록
    @PreAuthorize("hasRole('GUARDIAN')")
    @PostMapping("/children")
    public ResponseEntity<?> registerChild(@RequestBody ChildRegisterRequestDto dto,
                                           HttpServletRequest request) {
        String parentEmail = extractEmail(request);
        Long childId = userService.registerChild(parentEmail, dto);
        return ResponseEntity.ok("자녀 등록 완료 (ID: " + childId + ")");
    }

    // ✅ 자녀 목록 조회 (GET /api/guardian/children)
    @GetMapping("/children")
    public ResponseEntity<List<ChildResponse>> getChildren(@AuthenticationPrincipal User user) {
        List<User> children = userRepository.findByParent(user);

        List<ChildResponse> response = children.stream()
                .map(ChildResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }


    // ✅ 자녀 삭제 (DELETE /api/guardian/children/{childId})
    @DeleteMapping("/children/{childId}")
    public ResponseEntity<String> deleteChild(@PathVariable Long childId,
                                              @AuthenticationPrincipal User guardian) {
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new CustomException("자녀를 찾을 수 없습니다."));

        // 삭제 권한 확인: 현재 로그인한 보호자가 해당 자녀의 부모인지 확인
        if (!guardian.getId().equals(child.getParent().getId())) {
            throw new CustomException("삭제 권한이 없습니다.");
        }

        userRepository.delete(child);
        return ResponseEntity.ok("자녀 삭제 완료");
    }


}
