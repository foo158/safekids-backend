package com.safekids.backend.dto;

import com.safekids.backend.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChildResponse {
    private Long id;
    private String name;
    private String email;

    public static ChildResponse fromEntity(User user) {
        return new ChildResponse(user.getId(), user.getName(), user.getEmail());
    }
}
