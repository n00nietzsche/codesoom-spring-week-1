package com.codesoom.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserCreationDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;

    @Builder
    public UserCreationDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
