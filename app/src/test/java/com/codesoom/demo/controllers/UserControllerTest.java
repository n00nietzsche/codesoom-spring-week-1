package com.codesoom.demo.controllers;

import com.codesoom.demo.Utf8WebMvcTest;
import com.codesoom.demo.application.UserService;
import com.codesoom.demo.config.Config;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.dto.UserUpdateDto;
import com.codesoom.demo.exceptions.UserNotFoundException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        given(userService.updateUser(eq(1L), any(UserUpdateDto.class))).will((invocation -> {
            Long id = invocation.getArgument(0);
            UserUpdateDto userUpdateDto = invocation.getArgument(1);
            User user = User.builder()
                    .id(id)
                    .password("tester")
                    .email("tester@example.com")
                    .name("tester")
                    .build();
            mapper.map(userUpdateDto, user);
            return user;
        }));

        given(userService.updateUser(eq(1000L), any(UserUpdateDto.class))).will((invocation -> {
            Long id = invocation.getArgument(0);
            throw new UserNotFoundException(id);
        }));

        given(userService.updateUser(eq(2002L), any(UserUpdateDto.class))).will((invocation -> {
            Long id = invocation.getArgument(0);
            throw new UserNotFoundException(id);
        }));

        given(userService.deleteUser(any(Long.class))).will(invocation -> {
            Long id = invocation.getArgument(0);

            User user = User.builder()
                    .id(id)
                    .password("tester")
                    .email("tester@example.com")
                    .name("tester")
                    .build();

            user.delete();

            return user;
        });

        given(userService.deleteUser(eq(1000L))).will((invocation -> {
            Long id = invocation.getArgument(0);
            throw new UserNotFoundException(id);
        }));

        given(userService.deleteUser(eq(2002L))).will((invocation -> {
            Long id = invocation.getArgument(0);
            throw new UserNotFoundException(id);
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

    @Test
    void updateUser() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"tester\"}")
                ).andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1")
                ))
                .andExpect(content().string(
                        containsString("\"email\":\"tester@example.com\"")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"tester\"")
                ));

        verify(userService).updateUser(eq(1L), any(UserUpdateDto.class));
    }

    @Test
    void updateUserWithEmptyName() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}")
                ).andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithEmptyPassword() throws Exception {
        mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"asdfasdf\",\"password\":\"\"}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithNotFound() throws Exception {
        mockMvc.perform(patch("/users/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"tester\",\"password\":\"test\"}")
                ).andExpect(status().isNotFound());
    }

    @Test
    void updateDeletedUser() throws Exception {
        mockMvc.perform(patch("/users/2002")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"tester\",\"password\":\"test\"}")
        ).andExpect(status().isNotFound());
    }

    @Test
    void destroy() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    void destroyNotFoundUser() throws Exception {
        mockMvc.perform(delete("/users/1000"))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(1000L);
    }

    @Test
    void destroyDeletedUser() throws Exception {
        mockMvc.perform(delete("/users/2002"))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(2002L);
    }
}