package com.codesoom.demo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("changeWith 는 source 를 받아 name 만 반영한다.")
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
        assertThat(user.getPassword()).isNotEqualTo(source.getPassword());
        assertThat(user.getEmail()).isEqualTo("my@naver.com");
    }

    @Test
    void delete() {
        User user = User
                .builder()
                .name("name")
                .password("password")
                .build();

        assertThat(user.isDeleted()).isFalse();

        user.delete();

        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    void authenticate() {
        String password = "password";
        String wrongPassword = "wrong" + password;

        User user = User.builder()
                .email("myEmail@naver.com")
                .password(password)
                .build();

        assertThat(user.authenticate(password)).isTrue();
        assertThat(user.authenticate(wrongPassword)).isFalse();
    }

    @Test
    void changePassword() {
        User user = User
                .builder()
                .name("testUser")
                .password("Test")
                .build();

        assertThat(user.getPassword()).isNotEmpty();
        assertThat(user.getPassword()).isNotEqualTo("Test");
    }

    @Test
    @DisplayName("authenticate() 메서드는 삭제된 회원을 인증할 때 false 를 반환한다.")
    void authenticateWithDeletedUser() {
        String password = "password";

        User user = User.builder()
                .email("myEmail@naver.com")
                .password(password)
                .build();

        user.delete();

        assertThat(user.authenticate(password)).isFalse();
    }
}