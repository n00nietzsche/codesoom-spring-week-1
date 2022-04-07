package com.codesoom.demo.application;

import com.codesoom.demo.exceptions.TaskNotFoundException;
import com.codesoom.demo.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)

    private TaskService taskService;
    private final String TASK_TITLE = "test";
    private final String UPDATE_POSTFIX = "!!!";
    private Long firstTaskId;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskService.createTask(task);

        firstTaskId = taskService.getTasks().get(0).getId();
    }

    @Test
    void getTasks() {
        assertThat(taskService.getTasks()).hasSize(1);
    }

    @Test
    void getTaskWithValidId() {
        assertThat(taskService.getTask(firstTaskId).getTitle())
                .isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(1000L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createTask() {
        int oldSize = taskService.getTasks().size();
        assertThat(oldSize).isEqualTo(1);

        Task task = new Task();
        task.setTitle("new task");
        taskService.createTask(task);

        int newSize = taskService.getTasks().size();
        assertThat(newSize).isEqualTo(oldSize + 1);
    }

    @Test
    void deleteTask() {
        int oldSize = taskService.getTasks().size();

        taskService.deleteTask(firstTaskId);

        int newSize = taskService.getTasks().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }

    @Test
    void updateTask() {
        Task source = new Task();
        String taskTitle = TASK_TITLE + UPDATE_POSTFIX;

        source.setTitle(taskTitle);
        taskService.updateTask(firstTaskId, source);

        assertThat(taskService.getTask(firstTaskId).getTitle()).isEqualTo(taskTitle);
    }
}