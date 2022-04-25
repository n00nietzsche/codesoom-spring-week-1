package com.codesoom.demo.exceptions;

public class DuplicateUserEmailException extends RuntimeException {
    public DuplicateUserEmailException(String email) {
        super(String.format("%s 는 이미 가입된 이메일입니다.", email));
    }
}
