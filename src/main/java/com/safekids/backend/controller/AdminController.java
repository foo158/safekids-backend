package com.safekids.backend.controller;

import com.safekids.backend.dto.RoleUpdateRequestDto;
import com.safekids.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')") // 관리자만 접근 가능
    @PutMapping("/role")
    public ResponseEntity<String> updateUserRole(@RequestBody RoleUpdateRequestDto requestDto) {
        userService.updateUserRole(requestDto.getEmail(), requestDto.getNewRole());
        return ResponseEntity.ok("역할이 성공적으로 변경되었습니다.");
    }
}
