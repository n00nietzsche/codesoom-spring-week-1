package com.codesoom.demo.controllers;

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.application.TaskService;
import com.codesoom.demo.exceptions.TaskNotFoundException;
import com.codesoom.demo.domain.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// DONE: Read Collection - GET /tasks
// DONE: Read Item - GET /tasks/{id}
// DONE: Create - POST /tasks
// DONE: Update - PUT/PATCH /tasks/{id}
// DONE: Delete - DELETE /tasks/{id}

// 전제 -> 서비스가 올바른 것
// 서비스가 알아서 잘 할것이라고 믿으며 테스트를 작성했음.


/**
 * 여태까지 JUnit 만으로 했던 테스트와 달리, `@WebMvcTest` 는 스프링 웹 레이어만을 이용한 테스트를 한다.
 */
@WebMvcTest(TaskController.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class TaskControllerWebTest {
    @Autowired
    private MockMvc mockMvc;
    /*
    * 스프링은 컨테이너에 올라간 빈의 관계를 알고 있다.
    * DI 가 어떻게 되는지, 의존 관계를 알고 있다.
    * @MockBean 은 mockito 를 한번 더 감싼 것이다.
    * */
    @MockBean
    private TaskService taskService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        tasks.add(task1);
        task1.setTitle("Test Task");

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(1L)).willReturn(task1);

        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));

        given(taskService.createTask(task1)).willReturn(task1);

        given(taskService.updateTask(eq(100L), any(Task.class))).willThrow(new TaskNotFoundException(100L));

        given(taskService.deleteTask(100L)).willThrow(new TaskNotFoundException(100L));
    }

    @Test
    public void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Task")));

        verify(taskService).getTasks();
    }

    @Test
    public void detailFound() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService).getTask(any(Long.class));
    }

    @Test
    public void detailNotFound() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());

        verify(taskService).getTask(any(Long.class));
    }

    @Test
    public void createTask() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");
        String taskJson = objectMapper.writeValueAsString(task);

        mockMvc.perform(post("/tasks")
                        .content(taskJson) // 컨트롤러에서 받기 때문에, 없으면 400 (Bad Request)
                        .contentType(MediaType.APPLICATION_JSON) // 없으면 415 (Media Type Not Supported )
                )
                .andExpect(status().isCreated()); // 웹 환경에서 실제로 어떤 데이터를 받느냐는 중요하다.

        verify(taskService).createTask(any(Task.class));
    }

    @Test
    public void updateTaskFound() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");
        String taskJson = objectMapper.writeValueAsString(task);

        mockMvc.perform(patch("/tasks/1")
                        .content(taskJson) // 컨트롤러에서 받기 때문에, 없으면 400 (Bad Request)
                        .contentType(MediaType.APPLICATION_JSON) // 없으면 415 (Media Type Not Supported )
                )
                .andExpect(status().isOk()); // 웹 환경에서 실제로 어떤 데이터를 받느냐는 중요하다.

        verify(taskService).updateTask(eq(1L), any(Task.class));
    }

    @Test
    public void updateTaskNotFound() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");
        String taskJson = objectMapper.writeValueAsString(task);

        mockMvc.perform(patch("/tasks/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isNotFound()); // 웹 환경에서 실제로 어떤 데이터를 받느냐는 중요하다.

        verify(taskService).updateTask(eq(100L), any(Task.class));
    }

    @Test
    public void deleteTaskFound() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent()); // 웹 환경에서 실제로 어떤 데이터를 받느냐는 중요하다.

        verify(taskService).deleteTask(1L);
    }

    @Test
    public void deleteTaskNotFound() throws Exception {
        mockMvc.perform(delete("/tasks/100"))
                .andExpect(status().isNotFound()); // 웹 환경에서 실제로 어떤 데이터를 받느냐는 중요하다.

        verify(taskService).deleteTask(100L);
    }
}
