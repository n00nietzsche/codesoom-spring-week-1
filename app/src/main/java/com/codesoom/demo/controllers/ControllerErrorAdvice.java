package com.codesoom.demo.controllers;

import com.codesoom.demo.exceptions.DuplicateUserEmailException;
import com.codesoom.demo.exceptions.ProductNotFoundException;
import com.codesoom.demo.exceptions.TaskNotFoundException;
import com.codesoom.demo.dto.ErrorResponse;
import com.codesoom.demo.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// @RestControllerAdvice 도 알아보기
@ControllerAdvice
public class ControllerErrorAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND) // 상태 코드
    @ExceptionHandler(TaskNotFoundException.class) // target 클래스
    public ErrorResponse handleTaskNotFound() {
        return new ErrorResponse("Task not found");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND) // 상태 코드
    @ExceptionHandler(ProductNotFoundException.class) // target 클래스
    public ErrorResponse handleProductNotFound() {
        return new ErrorResponse("Product not found");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateUserEmailException.class)
    public ErrorResponse handleDuplicateUserEmail() {
        return new ErrorResponse("User's email address is duplicated");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found");
    }
}
