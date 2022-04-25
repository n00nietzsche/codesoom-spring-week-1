package com.codesoom.demo.controllers;

import com.codesoom.demo.Utf8WebMvcTest;
import com.codesoom.demo.application.UserService;
import com.codesoom.demo.config.Config;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.dto.UserCreationDto;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Utf8WebMvcTest(UserController.class)
@Import(Config.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Mapper mapper;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        given(userService.createUser(any(UserCreationDto.class))).will((invocation -> {
            UserCreationDto userDto = invocation.getArgument(0);
            return mapper.map(userDto, User.class);
        }));
    }

    @Test
    void createUserWithValidAttributes() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"name\":\"tester\",\"password\":\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"email\":\"tester@example.com\"")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"tester\"")
                ));

        verify(userService).createUser(any(UserCreationDto.class));
    }

    @Test
    void createUserWithInvalidAttributes() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"name\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}