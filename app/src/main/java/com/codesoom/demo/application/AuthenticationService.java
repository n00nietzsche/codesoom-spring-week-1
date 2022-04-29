package com.codesoom.demo.application;

import com.codesoom.demo.exceptions.InvalidAccessTokenException;
import com.codesoom.demo.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static org.springframework.util.StringUtils.*;

@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login(Long userId) {
        return jwtUtil.encode(userId);
    }

    public Long parseToken(String token) {
        if(!hasText(token)) {
            throw new InvalidAccessTokenException(token);
        }

        try {
            Claims claims = jwtUtil.decode(token);
            return claims.get("userId", Long.calass);
        } catch (JwtException e) {
            throw new InvalidAccessTokenException(token);
        }


    }
}
