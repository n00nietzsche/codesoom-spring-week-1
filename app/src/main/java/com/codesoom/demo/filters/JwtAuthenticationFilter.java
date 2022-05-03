package com.codesoom.demo.filters;

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.exceptions.InvalidAccessTokenException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final AuthenticationService authenticationService;
    private final Set<HttpMethod> targetMethods = new HashSet<>();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   AuthenticationService authenticationService) {
        super(authenticationManager);
        this.authenticationService = authenticationService;

        targetMethods.add(HttpMethod.POST);
        targetMethods.add(HttpMethod.PATCH);
        targetMethods.add(HttpMethod.DELETE);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        // interceptor 에서는 true 면 진행, false 면 스탑이었던 반면에
        // filter 에서는 다음 filter 를 순차적으로 chaining 호출해준다.
        // -> 아무 일도 하지 않으면, 여기서 체인이 멈추고 아무 일도 일어나지 않는다.

        // TODO: authentication 작업
        if (needAuthenticationFilter(request)) {
                doAuthentication(request);
                // Interceptor 에서는 예외는 그냥 던져버리면 스프링에서 잘 잡았는데...
                // filter 는 예외를 그냥 던져버렸을 때 스프링에서 잡질 못해서 Advice 적용이 안 된다.
        }

        super.doFilterInternal(request, response, chain);
    }

    private boolean needAuthenticationFilter(HttpServletRequest request) {
        HttpMethod method = HttpMethod.resolve(request.getMethod());

        return request.getRequestURI().startsWith("/products")
        && targetMethods.contains(method);
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
    }
}
