package com.codesoom.demo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void changeWith() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .password("pppp1234")
                .email("my@naver.com")
                .build();

        User source = User.builder()
                .name("tester")
                .password("password")
                .build();

        user.changeWith(source);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo(source.getName());
        assertThat(user.getPassword()).isEqualTo(source.getPassword());
        assertThat(user.getEmail()).isEqualTo("my@naver.com");
    }

    @Test
    void delete() {
        User user = User.builder().build();

        assertThat(user.isDeleted()).isFalse();

        user.delete();

        assertThat(user.isDeleted()).isTrue();
    }
}