package com.codesoom.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {
    private JwtUtil jwtUtil;
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDE";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void encode() {
        String accessToken = jwtUtil.encode(1L);
        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    void decodeWithValidToken() {
        // TODO: -> userId, verfication
        Claims claims = jwtUtil.decode(VALID_TOKEN);
        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @Test
    void decodeWithInvalidToken() {
        // TODO: -> userId, verfication
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    void decodeWithBlankToken() {
        // TODO: -> userId, verfication
        assertThatThrownBy(() -> jwtUtil.decode(""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}