package com.codesoom.demo.filters;

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.authentication.UserAuthentication;
import com.codesoom.demo.exceptions.InvalidAccessTokenException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final AuthenticationService authenticationService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   AuthenticationService authenticationService) {
        super(authenticationManager);
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        doAuthentication(request);
        super.doFilterInternal(request, response, chain);
    }

    private void doAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if(StringUtils.hasText(authorization)) {
            authorize(authorization);
        }
    }

    private void authorize(String authorization) {
        // DONE: userId 를 넘겨주자 -> Authentication 처리를 해보자.
        // SecurityContext 에 setAuthentication() 메서드를 통해 userId 를 넘겨줬다.
        String token = authorization.substring("Bearer ".length());
        Long userId = authenticationService.parseToken(token);
        Authentication authentication = new UserAuthentication(userId);
        // 완성되면 컨트롤러에서 스프링 HandlerMethodArgumentResolver 형태로 가져다가 쓸 수 있다.
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
    }
}
