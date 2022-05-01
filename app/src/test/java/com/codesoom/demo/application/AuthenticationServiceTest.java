package com.codesoom.demo.application;

import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.dto.UserLoginDto;
import com.codesoom.demo.exceptions.InvalidAccessTokenException;
import com.codesoom.demo.exceptions.LoginFailException;
import com.codesoom.demo.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
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
    void loginWithRightEmailAndPassword() {
        String accessToken = authenticationService.login(
                UserLoginDto.builder()
                        .email(RIGHT_EMAIL)
                        .password(RIGHT_PASSWORD)
                        .build()
        );

        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    void loginWithWrongPassword() {
         assertThatThrownBy(() -> {
             authenticationService.login(
                     UserLoginDto.builder()
                             .email(RIGHT_EMAIL)
                             .password(WRONG_PASSWORD)
                             .build());
         }).isInstanceOf(LoginFailException.class);
    }

    @Test
    void loginWithWrongEmail() {
        assertThatThrownBy(() -> {
            authenticationService.login(
                    UserLoginDto.builder()
                            .email(WRONG_EMAIL)
                            .password(RIGHT_PASSWORD)
                            .build());
        }).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void parseValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseNullToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(null))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

    @Test
    void parseEmptyToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(""))
                .isInstanceOf(InvalidAccessTokenException.class);

        assertThatThrownBy(() -> authenticationService.parseToken("     "))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

    @Test
    void parseInvalidToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidAccessTokenException.class);
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