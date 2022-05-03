package com.codesoom.demo.filters;

import com.codesoom.demo.exceptions.InvalidAccessTokenException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Filter 를 전부 구현하는 것이 아니라,
// HTTP 에 관련된 부분만 구현하고 싶다면, HttpFilter 추상 클래스를 사용한다.
// Filter 를 전부 구현하고 싶다면, Filter 인터페이스를 구현하면 된다.
public class AuthenticationErrorFilter extends HttpFilter {
    @Override
    protected void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        try {
            super.doFilter(request, response, chain);
        } catch (InvalidAccessTokenException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
