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
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;
    private final Set<HttpMethod> applyToMethods = new HashSet<>();

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        
        applyToMethods.add(HttpMethod.POST);
        applyToMethods.add(HttpMethod.PATCH);
        applyToMethods.add(HttpMethod.DELETE);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        HttpMethod method = HttpMethod.resolve(request.getMethod());

        if(applyToMethods.contains(method)) {
            doAuthentication(request);
        }

        // true 면 계속 진행, false 면 중단하는 것이다.
        return true;
    }

    private void doAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        checkIfAuthorized(authorization);
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
