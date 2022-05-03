package com.codesoom.demo.config;

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.filters.AuthenticationErrorFilter;
import com.codesoom.demo.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.Filter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationService authenticationService;

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
                .addFilterBefore(authenticationErrorFilter, JwtAuthenticationFilter.class);
    }
}
