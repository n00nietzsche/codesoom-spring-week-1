package com.codesoom.demo.controllers;

import com.codesoom.demo.Utf8WebMvcTest;
import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.application.UserService;
import com.codesoom.demo.config.Config;
import com.codesoom.demo.config.SecurityConfig;
import com.codesoom.demo.domain.Role;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.dto.UserUpdateDto;
import com.codesoom.demo.exceptions.UserNotFoundException;
import com.codesoom.demo.utils.JwtUtil;
import com.github.dozermapper.core.Mapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
@Import({Config.class, SecurityConfig.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Mapper mapper;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationService authenticationService;

    private static final String SECRET = "12345678901234567890123456789012";
    // userId ??? 1 ??? ??????
    private static final String USER_ONE_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    // userId ??? 2 ??? ??????
    private static final String USER_TWO_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjJ9.TEM6MULsZeqkBbUKziCR4Dg_8kymmZkyxsCXlfNJ3g0";
    // userId ??? 1004 ??? ??????
    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEwMDR9.3GV5ZH3flBf0cnaXQCNNZlT4mgyFyBUhn3LKzQohh1A";

    @BeforeEach
    void setUp() {
        given(authenticationService.parseToken(any(String.class))).will((invocation -> {
            JwtUtil jwtUtil = new JwtUtil(SECRET);
            String token = invocation.getArgument(0);
            Claims claims = jwtUtil.decode(token);

            return claims.get("userId", Long.class);
        }));

        given(authenticationService.getRoles(1L)).willReturn(
                Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN"))
        );

        given(userService.createUser(any(UserCreationDto.class))).will((invocation -> {
            UserCreationDto userDto = invocation.getArgument(0);
            return mapper.map(userDto, User.class);
        }));

        given(userService.updateUser(eq(1L), any(UserUpdateDto.class), eq(1L))).will((invocation -> {
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

        given(userService.updateUser(eq(1000L), any(UserUpdateDto.class), any(Long.class))).will((invocation -> {
            Long id = invocation.getArgument(0);
            throw new UserNotFoundException(id);
        }));

        given(userService.updateUser(eq(1L), any(UserUpdateDto.class), eq(2L))).will((invocation -> {
            Long id = invocation.getArgument(0);
            throw new AccessDeniedException("?????? ???????????? ????????? ????????? ??? ????????????.");
        }));

        given(userService.updateUser(eq(2002L), any(UserUpdateDto.class), any(Long.class))).will((invocation -> {
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
                        .header("Authorization", "Bearer " + USER_ONE_TOKEN)
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

        verify(userService).updateUser(eq(1L), any(UserUpdateDto.class), eq(1L));
    }

    @Test
    void updateUserWithoutToken() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"tester\"}")
                ).andExpect(status().isUnauthorized());
    }

    @Test
    void updateUserWithNotItsOwnToken() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"tester\"}")
                        .header("Authorization", "Bearer " + USER_TWO_TOKEN)
                ).andExpect(status().isForbidden());
    }

    @Test
    void updateUserWithEmptyName() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}")
                        .header("Authorization", "Bearer " + USER_ONE_TOKEN)
                ).andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithEmptyPassword() throws Exception {
        mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"asdfasdf\",\"password\":\"\"}")
                .header("Authorization", "Bearer " + USER_ONE_TOKEN)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithNotFound() throws Exception {
        mockMvc.perform(patch("/users/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"tester\",\"password\":\"test\"}")
                        .header("Authorization", "Bearer " + USER_ONE_TOKEN)
                ).andExpect(status().isNotFound());
    }

    @Test
    void updateDeletedUser() throws Exception {
        mockMvc.perform(patch("/users/2002")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"tester\",\"password\":\"test\"}")
                .header("Authorization", "Bearer " + USER_ONE_TOKEN)
        ).andExpect(status().isNotFound());
    }

    @Test
    void destroy() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .header("Authorization", "Bearer " + USER_ONE_TOKEN))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    void destroyWithoutAdminRole() throws Exception {
        mockMvc.perform(delete("/users/2")
                        .header("Authorization", "Bearer " + USER_TWO_TOKEN))
                .andExpect(status().isForbidden());
    }

    @Test
    void destroyWithoutToken() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void destroyNotFoundUser() throws Exception {
        mockMvc.perform(delete("/users/1000")
                        .header("Authorization", "Bearer " + USER_ONE_TOKEN))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(1000L);
    }

    @Test
    void destroyDeletedUser() throws Exception {
        mockMvc.perform(delete("/users/2002")
                        .header("Authorization", "Bearer " + USER_ONE_TOKEN))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(2002L);
    }

    @Test
    void roles() {
        assertThat(
                authenticationService.getRoles(1L)
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        ).isEqualTo(Arrays.asList("USER", "ADMIN"));
    }
}