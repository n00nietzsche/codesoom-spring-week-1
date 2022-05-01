package com.codesoom.demo.controllers;

import com.codesoom.demo.exceptions.*;
import com.codesoom.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// @RestControllerAdvice 도 알아보기
@ControllerAdvice
@ResponseBody
public class ControllerErrorAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND) // 상태 코드
    @ExceptionHandler(TaskNotFoundException.class) // target 클래스
    public ErrorResponse handleTaskNotFound() {
        return new ErrorResponse("Task not found");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) // 상태 코드
    @ExceptionHandler(ProductNotFoundException.class) // target 클래스
    public ErrorResponse handleProductNotFound() {
        return new ErrorResponse("Product not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateUserEmailException.class)
    public ErrorResponse handleDuplicateUserEmail() {
        return new ErrorResponse("User's email address is duplicated");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyDeletedUserException.class)
    public ErrorResponse handleAlreadyDeletedUser() {
        return new ErrorResponse("this user is already deleted");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidAccessTokenException.class)
    public void handleInvalidAccessTokenException() {

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailException.class)
    public void handleLoginFailException() {

    }

}
