package com.codesoom.demo.controllers;

import com.codesoom.demo.exceptions.TaskNotFoundException;
import com.codesoom.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// @RestControllerAdvice 도 알아보기
@ControllerAdvice
public class TaskErrorAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND) // 상태 코드
    @ExceptionHandler(TaskNotFoundException.class) // target 클래스
    public ErrorResponse handleNotFound() {
        return new ErrorResponse("Task not found");
    }
}
