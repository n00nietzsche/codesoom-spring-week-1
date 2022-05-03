package com.codesoom.demo.filters;

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.authentication.UserAuthentication;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

        // TODO: 경로나 메서드에 따라 처리되는 것을 좀 더 추상화할 수 없을까..?
        doAuthentication(request);
        // Interceptor 에서는 예외는 그냥 던져버리면 스프링에서 잘 잡았는데...
        // filter 는 예외를 그냥 던져버렸을 때 스프링에서 잡질 못해서 Advice 적용이 안 된다.

        super.doFilterInternal(request, response, chain);
    }

    private void doAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if(authorization != null) {
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
