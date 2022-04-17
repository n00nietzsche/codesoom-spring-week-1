package com.codesoom.demo;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.annotation.*;
import java.nio.charset.StandardCharsets;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WebMvcTest
@Import(Utf8WebMvcTest.Config.class)
public @interface Utf8WebMvcTest {
    @AliasFor("controllers")
    Class<?>[] value() default {};

    @AliasFor("value")
    Class<?>[] controllers() default {};

    class Config {

        @Bean
        public CharacterEncodingFilter characterEncodingFilter() {
            return new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true);
        }
    }
}
