package com.safekids.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildRegisterRequestDto {
    private String name;
    private String email;
    private String password;
}
