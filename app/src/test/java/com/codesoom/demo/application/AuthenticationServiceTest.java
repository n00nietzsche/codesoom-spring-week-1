package com.codesoom.demo.application;

import com.codesoom.demo.exceptions.InvalidAccessTokenException;
import com.codesoom.demo.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDK";

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(new JwtUtil(SECRET));
    }

    @Test
    void login() {
        String accessToken = authenticationService.login(1L);
        assertThat(accessToken).isEqualTo(VALID_TOKEN);
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
}