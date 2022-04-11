package com.codesoom.demo.controllers;

import com.codesoom.demo.application.TaskService;
import com.codesoom.demo.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class TaskControllerTest {
    // TODO: Read Collection - GET /tasks
    // TODO: Read Item - GET /tasks/{id}
    // TODO: Create - POST /tasks
    // TODO: Update - PUT/PATCH /tasks/{id}
    // TODO: Delete - DELETE /tasks/{id}

    private TaskController taskController;

    // 가능한 것
    // 1. Real Object
    // 2. Mock Object
    // 3. Spy -> Proxy pattern
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = spy(new TaskService());
        taskController = new TaskController(taskService);
    }

    @Test
    void listWithoutTasks() {
        // TODO: service -> returns empty list
        given(taskService.getTasks()).willReturn(new ArrayList<>());

        // taskService.getTasks
        // 인터페이스만 일치시켜 Proxy 로 한다면? Controller -> Spy -> Real Object

        assertThat(taskController.list()).isEmpty();
        verify(taskService).getTasks();
    }

    @Test
    void listWithSomeTasks() {
        // TODO: service -> returns list that contains one task.
        Task task = new Task();
        task.setTitle("Test1");
        taskController.create(task);

        assertThat(taskController.list()).isNotEmpty();
    }

    @Test
    void list() {
        assertThat(taskController.list()).isEmpty();
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Test1");
        taskController.create(task);
        task.setTitle("Test2");
        taskController.create(task);

        // 기억이 잘 안날 때는 앞선 키워드를 입력하면서 확인해볼 수 있다.
        // 혹은 공식문서 https://assertj.github.io/doc/ 를 참조하자.
        assertThat(taskController.list()).isNotEmpty();
        assertThat(taskController.list()).hasSize(2);

        assertThat(taskController.list().get(0).getTitle()).isEqualTo("Test1");
        assertThat(taskController.list().get(1).getTitle()).isEqualTo("Test2");
    }
}