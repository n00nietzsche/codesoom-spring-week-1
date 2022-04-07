package com.codesoom.demo.controllers;

import com.codesoom.demo.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {
    private TaskController taskController;

    @BeforeEach
    void setUp() {
         taskController = new TaskController();
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

        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo("Test1");

        assertThat(taskController.list().get(1).getId()).isEqualTo(2L);
        assertThat(taskController.list().get(1).getTitle()).isEqualTo("Test2");
    }
}