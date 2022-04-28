package com.codesoom.demo.application;

import com.codesoom.demo.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private static final String SECRET = "12345678901234567890123456789012";

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(new JwtUtil(SECRET));
    }

    @Test
    void login() {
        String accessToken = authenticationService.login();
        assertThat(accessToken).contains(".");
    }
}