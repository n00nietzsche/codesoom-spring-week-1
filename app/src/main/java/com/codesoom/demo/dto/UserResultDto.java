package com.codesoom.demo.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResultDto {
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String name;

    @Builder
    public UserResultDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
