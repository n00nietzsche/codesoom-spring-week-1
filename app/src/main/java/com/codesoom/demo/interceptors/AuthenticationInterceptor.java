package com.codesoom.demo.interceptors;

// CLIENT 입장
// Authentication -> 로그인 -> Token (인증)
// Authorization <- Token
// 토큰을 받고 받은 토큰을 이용해서 계속 인가를 받음.

// SERVER 입장
// Authentication -> 로그인 (인증)
// Token -> Authentication (인증)
// User -> Role -> Authorization (인가)
// 토큰을 주고 매번 토큰을 다시 받아서 인증을 하고, 해당 사용자에게 권한이 있는지 체크 후 인가를 함.

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.exceptions.InvalidAccessTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String authorization = request.getHeader("Authorization");
        checkIfAuthorized(authorization);

        return true;
    }

    private void checkIfAuthorized(String authorization) {
        if(isEmpty(authorization)) {
            throw new InvalidAccessTokenException("로그인이 필요합니다.");
        }

        String token = authorization.substring("Bearer ".length());
        Long userId = authenticationService.parseToken(token);
        System.out.println("userId = " + userId);
    }
}
