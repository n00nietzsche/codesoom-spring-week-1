package com.codesoom.demo.controllers;

// TODO: 가입 -> POST /users -> 가입 정보 (DTO) -> email 이 unique key 가 될 것임
// TODO: 목록, 상세보기 -> ADMIN
// TODO: 사용자 정보 갱신 -> PUT/PATCH /users/{id} -> 정보 갱신 (DTO) -> 이름만 바꿀 수 있도록
// TODO: 탈퇴 -> DELETE /users/{id}

import com.codesoom.demo.application.UserService;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.dto.UserResultDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public UserResultDto create(@RequestBody @Valid UserCreationDto userCreationDto) {
        // TODO: UserService 를 통해 User 를 생성할 것이다.
        User user = userService.createUser(userCreationDto);

        return UserResultDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
