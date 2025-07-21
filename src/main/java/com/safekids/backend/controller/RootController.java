package com.safekids.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> rootMessage() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "SafeKids 백엔드 서버가 정상 작동 중입니다.");
        return response;
    }
}
