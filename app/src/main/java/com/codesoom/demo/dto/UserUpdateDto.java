package com.codesoom.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {
    @NotBlank
    @Size(min = 1, message = "이름은 1글자 이상이어야 합니다.")
    private String name;
    @Size(min = 4, max = 20, message = "패스워드는 4자 이상 20자 이하입니다.")
    private String password;

    @Builder
    public UserUpdateDto(String name, String password) {
        this.name = name;
        this.password = password;
    }
}