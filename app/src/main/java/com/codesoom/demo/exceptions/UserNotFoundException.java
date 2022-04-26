package com.codesoom.demo.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format("id: %d 에 해당하는 유저를 찾을 수 없습니다.", id));
    }
}
