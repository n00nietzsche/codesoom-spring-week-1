package com.codesoom.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor @Builder
public class SessionResponseDto {
    private String accessToken;

}
