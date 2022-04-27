package com.codesoom.demo.application;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.dto.UserUpdateDto;
import com.codesoom.demo.exceptions.DuplicateUserEmailException;
import com.codesoom.demo.exceptions.UserNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    private final UserService userService;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    private final String DUPLICATE_EMAIL_ADDRESS = "duplicate@example.com";

    public UserServiceTest() {
        this.userService = new UserService(mapper, userRepository);;

        given(userRepository.save(any(User.class))).will(invocation -> {
            User source = invocation.getArgument(0);

            return User.builder()
                    .id(13L)
                    .name(source.getName())
                    .email(source.getEmail())
                    .password(source.getPassword())
                    .build();
        });

        given(userRepository.existsByEmail(DUPLICATE_EMAIL_ADDRESS))
                .willReturn(true);

        given(userRepository.findByIdAndDeletedIsFalse(any(Long.class))).will(invocation -> {
            Long id = invocation.getArgument(0);

            return Optional.of(User.builder()
                    .id(id)
                    .name("tester")
                    .email("tester@example.com")
                    .password("12341234")
                    .build());
        });

        given(userRepository.findByIdAndDeletedIsFalse(eq(1000L))).willReturn(Optional.empty());
    }

    @Test
    void createUser() {
        UserCreationDto userCreationDto = UserCreationDto.builder()
                .name("tester")
                .email("tester@example.com")
                .password("test")
                .build();

        User user = userService.createUser(userCreationDto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(13L);
        assertThat(user.getName()).isEqualTo("tester");
        assertThat(user.getEmail()).isEqualTo("tester@example.com");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUserWithDuplicateEmail() {

        UserCreationDto userCreationDto = UserCreationDto.builder()
                .name("tester")
                .email(DUPLICATE_EMAIL_ADDRESS)
                .password("test")
                .build();

        assertThatThrownBy(() -> userService.createUser(userCreationDto))
                .isInstanceOf(DuplicateUserEmailException.class);

        verify(userRepository).existsByEmail(DUPLICATE_EMAIL_ADDRESS);
    }

    @Test
    void updateUserWithExistingId() {
        UserUpdateDto userUpdateDto = UserUpdateDto
                .builder()
                .name("updatedTester")
                .password("updatedTester123")
                .build();

        User user = userService.updateUser(1L, userUpdateDto);
        assertThat(user.getName()).isEqualTo("updatedTester");
        assertThat(user.getPassword()).isEqualTo("updatedTester123");
        assertThat(user.getEmail()).isEqualTo("tester@example.com");

        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void updateUserWithNotFoundId() {
        UserUpdateDto userUpdateDto = UserUpdateDto
                .builder()
                .name("updatedTester")
                .password("updatedTester123")
                .build();

        assertThatThrownBy(() -> userService.updateUser(1000L, userUpdateDto))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(1000L);
    }

    @Test
    void deleteUserWithExistingId() {
        User user = userService.deleteUser(1L);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.isDeleted()).isTrue();
    }
}