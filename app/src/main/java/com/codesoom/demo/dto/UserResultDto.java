package com.codesoom.demo.dto;

import com.codesoom.demo.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResultDto {
    private Long id;
    private String email;
    private String name;

    @Builder
    public UserResultDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserResultDto valueOf(User user) {
        if (user == null) {
            return null;
        }

        return UserResultDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
