package com.codesoom.demo.application;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.dto.UserLoginDto;
import com.codesoom.demo.exceptions.InvalidAccessTokenException;
import com.codesoom.demo.exceptions.LoginFailException;
import com.codesoom.demo.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static org.springframework.util.StringUtils.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public String login(UserLoginDto userLoginDto) {
        User user = userService.getUser(userLoginDto.getEmail());

        if(user.authenticate(userLoginDto.getPassword())) {
            return jwtUtil.encode(user.getId());
        }

        throw new LoginFailException(user.getEmail());
    }

    public Long parseToken(String token) {
        if(!hasText(token)) {
            throw new InvalidAccessTokenException(token);
        }

        try {
            Claims claims = jwtUtil.decode(token);
            return claims.get("userId", Long.class);
        } catch (JwtException e) {
            throw new InvalidAccessTokenException(token);
        }
    }
}
