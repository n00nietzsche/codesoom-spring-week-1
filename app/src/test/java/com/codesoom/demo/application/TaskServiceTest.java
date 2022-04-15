package com.codesoom.demo.application;

import com.codesoom.demo.domain.TaskRepository;
import com.codesoom.demo.exceptions.TaskNotFoundException;
import com.codesoom.demo.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * taskService 가 taskRepository 를 제대로 활용하고 있는지 검증한다.
 * verify() 메서드를 통해 실제로 taskRepository 의 메서드를 호출하는지 검증한다.
 *
 * 이 테스트를 작성하면, TaskService 클래스가 실제로 어떤 의존관계를 갖고 있는지에 대해 알게 된다.
 * 이는 의존 관계를 드러내는 테스트이며, 구현에 대해서는 전혀 검증하지 않는다.
 * 구현에 대해 드러내는 목적이 없기 때문에, 구현이 드러나면 잘못된 것이다.
 */
class TaskServiceTest {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)

    private TaskService taskService;
    private TaskRepository taskRepository;

    private final String TASK_TITLE = "test";
    private final String CREATE_POSTFIX = "...";
    private final String UPDATE_POSTFIX = "!!!";
    private final Long FIRST_ID = 1L;
    private final Long NOT_FOUND_ID = 1000L;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
        setUpFixtures();
        setUpSaveTask();
    }

    void setUpFixtures() {
        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setId(FIRST_ID);
        task.setTitle(TASK_TITLE);
        tasks.add(task);

        given(taskRepository.findAll()).willReturn(tasks);
        given(taskRepository.findById(FIRST_ID)).willReturn(Optional.of(task));
        given(taskRepository.findById(NOT_FOUND_ID)).willThrow(TaskNotFoundException.class);
    }

    void setUpSaveTask() {
        given(taskRepository.save(any(Task.class))).will(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(2L);
            return task;
        });
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskService.getTasks();
        // taskRepository 에서 findAll() 을 호출하는지, 사이즈가 1인지 확인
        verify(taskRepository).findAll();
        assertThat(tasks).hasSize(1);

        // 1번째 Task 를 꺼내면, TASK_TITLE 이 일치하는지 확인
        Task task = tasks.get(0);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithValidId() {
        Task task = taskService.getTask(FIRST_ID);
        verify(taskRepository).findById(FIRST_ID);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> taskService.getTask(NOT_FOUND_ID))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findById(NOT_FOUND_ID);
    }

    /**
     * 이제는 taskService 에서 Task 를 제대로 생성하는지 알 필요 없다.
     * taskRepository 의 save() 메서드가 정상적으로 호출된다면, taskService 는 당연히 정상적으로 Task 를 생성할 것이다.
     * taskRepository 의 테스트를 나중에 따로 만드는 것이 훨씬 정확할 것이다.
     */
    @Test
    void createTask() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + CREATE_POSTFIX);

        Task task = taskService.createTask(source);

        verify(taskRepository).save(any(Task.class));

        assertThat(task.getId()).isEqualTo(2L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + CREATE_POSTFIX);
    }

    @Test
    void updateTask() {
        Task source = new Task();
        String taskTitle = TASK_TITLE + UPDATE_POSTFIX;

        source.setTitle(taskTitle);
        Task task = taskService.updateTask(FIRST_ID, source);

        verify(taskRepository).findById(FIRST_ID);

        assertThat(task.getTitle()).isEqualTo(taskTitle);
    }

    @Test
    void updateTaskWithValidId() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);

        assertThatThrownBy(() -> taskService.updateTask(NOT_FOUND_ID, source))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findById(NOT_FOUND_ID);
    }

    @Test
    void deleteTask() {
        taskService.deleteTask(FIRST_ID);

        verify(taskRepository).findById(FIRST_ID);
        verify(taskRepository).delete(any(Task.class));
    }

    @Test
    void deleteTaskWithValidId() {
        assertThatThrownBy(() -> taskService.deleteTask(NOT_FOUND_ID))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository).findById(NOT_FOUND_ID);
    }
}