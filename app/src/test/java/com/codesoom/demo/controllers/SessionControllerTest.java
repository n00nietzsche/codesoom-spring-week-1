package com.codesoom.demo.controllers;

import com.codesoom.demo.annotations.Utf8MockMvc;
import com.codesoom.demo.application.UserService;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserCreationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Utf8MockMvc
class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private static final String RIGHT_EMAIL = "i.am.very.good.test.user@test.com";
    private static final String RIGHT_PASSWORD = "nobody.knows.my.password";
    private static final String WRONG_EMAIL = RIGHT_EMAIL + "d";
    private static final String WRONG_PASSWORD = RIGHT_PASSWORD + "d";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDK";

    @BeforeEach
    void setUp() {
        setUpTestUser();
    }

    @Test
    void loginWithRightEmailAndPassword() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + RIGHT_EMAIL + "\",\"password\":\"" + RIGHT_PASSWORD + "\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));
    }

    @Test
    void loginWithRightEmailAndWrongPassword() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + RIGHT_EMAIL + "\",\"password\":\"" + WRONG_PASSWORD + "\"}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginWithWrongEmailAndAnyPassword() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + WRONG_EMAIL + "\",\"password\":\"" + RIGHT_PASSWORD + "\"}")
                )
                .andExpect(status().isNotFound());
    }

    void setUpTestUser() {
        userRepository.deleteAll();

        userService.createUser(UserCreationDto.builder()
                .email(RIGHT_EMAIL)
                .password(RIGHT_PASSWORD)
                .name("SUCH A GOOD NAME")
                .build());
    }
}