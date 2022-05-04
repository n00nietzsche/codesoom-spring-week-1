package com.codesoom.demo.config;

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.filters.AuthenticationErrorFilter;
import com.codesoom.demo.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.servlet.Filter;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationService authenticationService;
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // "authenticationManager() 메서드는 어디서 튀어나온 거야?"
        // -> "WebSecurityConfigurerAdapter 추상 클래스가 원래 가지고 있었어."
        Filter authenticationFilter = new JwtAuthenticationFilter(
                authenticationManager(), authenticationService
        );

        Filter authenticationErrorFilter = new AuthenticationErrorFilter();
        http.csrf().disable()
                .addFilter(authenticationFilter)
                .addFilterBefore(authenticationErrorFilter, // advice 에서 잡지 않고, addFilterBefore() 에서 예외를 잡아주었다.
                        JwtAuthenticationFilter.class)
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED) // 권한 인증이 실패했을 때의 기본 상태코드
                );
    }
}
