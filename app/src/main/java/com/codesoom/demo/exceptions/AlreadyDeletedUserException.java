package com.codesoom.demo.exceptions;

public class AlreadyDeletedUserException extends RuntimeException {
    public AlreadyDeletedUserException(Long id) {
        super(String.format("id: %d 사용자는 이미 삭제되었습니다.", id));
    }
}
