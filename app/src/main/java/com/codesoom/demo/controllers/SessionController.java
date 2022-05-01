// REST
// TODO: CREATE -> 로그인 -> 토큰 돌려주기
// TODO: READ (w/ Token) -> 세션 정보 -> 이 토큰은 유효한가?
// TODO: UPDATE (w/ Token) -> 토큰 재발급
// TODO: DELETE -> 로그아웃 -> 토큰 무효화
// sessions (복수형) -> session (단수형) 사용.

package com.codesoom.demo.controllers;

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.dto.SessionResponseDto;
import com.codesoom.demo.dto.UserLoginDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionController {
    private final AuthenticationService authenticationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SessionResponseDto login(@RequestBody @Valid UserLoginDto userLoginDto) {
        String accessToken = authenticationService.login(userLoginDto);

        // 토큰을 주어야 함
        return SessionResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
