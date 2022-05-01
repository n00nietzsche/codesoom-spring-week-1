package com.codesoom.demo.application;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.dto.UserLoginDto;
import com.codesoom.demo.exceptions.InvalidAccessTokenException;
import com.codesoom.demo.exceptions.LoginFailException;
import com.codesoom.demo.exceptions.UserNotFoundException;
import com.codesoom.demo.utils.JwtUtil;
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
    @Autowired
    private JwtUtil jwtUtil;

    private static final String RIGHT_EMAIL = "i.am.very.good.test.user@test.com";
    private static final String RIGHT_PASSWORD = "nobody.knows.my.password";
    private static final String WRONG_EMAIL = RIGHT_EMAIL + "d";
    private static final String WRONG_PASSWORD = RIGHT_PASSWORD + "d";
    private String validToken;
    private String invalidToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        setUpTestUserAndToken();
    }

    @Test
    void loginWithRightEmailAndPassword() {
        String accessToken = authenticationService.login(
                UserLoginDto.builder()
                        .email(RIGHT_EMAIL)
                        .password(RIGHT_PASSWORD)
                        .build()
        );

        assertThat(accessToken).isEqualTo(validToken);
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
        Long userId = authenticationService.parseToken(validToken);
        assertThat(userId).isEqualTo(testUser.getId());
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
        assertThatThrownBy(() -> authenticationService.parseToken(invalidToken))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

    void setUpTestUserAndToken() {
        userRepository.deleteAll();

        testUser = userService.createUser(UserCreationDto.builder()
                .email(RIGHT_EMAIL)
                .password(RIGHT_PASSWORD)
                .name("SUCH A GOOD NAME")
                .build());

        validToken = jwtUtil.encode(testUser.getId());
        invalidToken = validToken + "d";
    }
}