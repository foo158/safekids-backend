package com.safekids.backend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateRequestDto {

    private String email;
    private String newRole;
}
