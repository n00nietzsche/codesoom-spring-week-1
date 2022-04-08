package com.codesoom.demo.controllers;

import com.codesoom.demo.application.TaskService;
import com.codesoom.demo.exceptions.TaskNotFoundException;
import com.codesoom.demo.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 여태까지 JUnit 만으로 했던 테스트와 달리, `@WebMvcTest` 는 스프링을 이용한 테스트를 한다.
 */
@WebMvcTest
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

    @BeforeEach
    public void setUp() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        tasks.add(task1);
        task1.setTitle("Test Task");

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(1L)).willReturn(task1);

        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
    }

    @Test
    public void list() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Task")));
    }

    @Test
    public void detailWithValidId() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void detailWithInValidId() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }
}
