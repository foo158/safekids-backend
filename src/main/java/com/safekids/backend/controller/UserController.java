package com.safekids.backend.controller;

import com.safekids.backend.dto.LoginRequestDto;
import com.safekids.backend.dto.LoginResponseDto;
import com.safekids.backend.dto.UserSignupRequestDto;
import com.safekids.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserSignupRequestDto requestDto) {
        Long userId = userService.signup(requestDto);
        return ResponseEntity.ok("회원가입 성공. ID: " + userId);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResponseDto response = userService.login(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponseDto> getMyInfo(@AuthenticationPrincipal String email) {
        LoginResponseDto response = userService.getMyInfo(email);
        return ResponseEntity.ok(response);
    }


}
