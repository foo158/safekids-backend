package com.safekids.backend.dto;

import com.safekids.backend.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String name;
    private String email;
    private User.Role role;

    public LoginResponseDto(User user, String token) {
        this.token = token;
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = User.Role.valueOf(user.getRole().name());
    }
}
