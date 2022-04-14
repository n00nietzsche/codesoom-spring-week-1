package com.codesoom.demo.controllers;

import com.codesoom.demo.application.TaskService;
import com.codesoom.demo.exceptions.TaskNotFoundException;
import com.codesoom.demo.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TaskControllerTest {
    // DONE: Read Collection - GET /tasks
    // DONE: Read Item - GET /tasks/{id}
    // DONE: Create - POST /tasks
    // DONE: Update - PUT/PATCH /tasks/{id}
    // DONE: Delete - DELETE /tasks/{id}

    // 전제 -> 서비스가 올바른 것
    // 서비스가 알아서 잘 할것이라고 믿으며 테스트를 작성했음.

    private TaskController taskController;

    // 가능한 것
    // 1. Real Object
    // 2. Mock Object / 타입이 필요하다.
    // 3. Spy -> Proxy pattern / 진짜 오브젝트가 필요하다.
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);

        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test1");
        tasks.add(task);

        // 실제 존재하는 리스트를 얻기
        given(taskService.getTasks()).willReturn(tasks);

        // 존재하는 Task 를 얻기
        given(taskService.getTask(1L)).willReturn(task);

        // 존재하지 않는 Task 를 얻기
        given(taskService.getTask(100L)).willThrow(TaskNotFoundException.class);

        // 존재하지 않는 Task 를 업데이트
        given(taskService.updateTask(eq(100L), any(Task.class))).willThrow(TaskNotFoundException.class);

        // 존재하지 않는 Task 를 삭제
        given(taskService.deleteTask(100L)).willThrow(TaskNotFoundException.class);
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
        assertThat(taskController.list()).isNotEmpty();
        verify(taskService).getTasks();
    }

    @Test
    void list() {
        given(taskService.getTasks()).willReturn(new ArrayList<>());
        assertThat(taskController.list()).isEmpty();
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Test1");
        taskController.create(task);
        verify(taskService).createTask(task);
    }

    @Test
    void detailFound() {
        Task task = taskController.detail(1L);
        assertThat(task).isNotNull();
        verify(taskService).getTask(1L);
    }

    @Test
    void detailNotFound() {
        assertThatThrownBy(() -> {
            taskController.detail(100L);
        });

        verify(taskService).getTask(100L);
    }

    @Test
    void updateTaskFound() {
        Task task = new Task();
        task.setTitle("Renamed task");

        taskController.update(1L, task);

        verify(taskService).updateTask(1L, task);
    }

    @Test
    void updateTaskNotFound() {
        Task task = new Task();
        task.setTitle("Renamed task");

        assertThatThrownBy(() -> taskController.update(100L, new Task()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTaskFound() {
        taskController.delete(1L);
        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteTaskNotFound() {
        assertThatThrownBy(() -> taskController.delete(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}