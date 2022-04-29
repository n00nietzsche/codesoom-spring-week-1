package com.codesoom.demo.config;

import com.codesoom.demo.utils.JwtUtil;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${jwt.secret}")
    private String SECRET;

    @Bean
    public Mapper mapper() {
        return DozerBeanMapperBuilder.buildDefault();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(SECRET);
    }
}
