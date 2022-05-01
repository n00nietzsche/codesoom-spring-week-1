package com.codesoom.demo.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class UserLoginDto {
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    @Builder
    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
