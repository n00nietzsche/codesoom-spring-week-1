package com.codesoom.demo.application;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.dto.UserUpdateDto;
import com.codesoom.demo.exceptions.DuplicateUserEmailException;
import com.codesoom.demo.exceptions.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;

    public User createUser(UserCreationDto userCreationDto) {
        if(userRepository.existsByEmail(userCreationDto.getEmail())) {
            throw new DuplicateUserEmailException(userCreationDto.getEmail());
        }

        User user = userRepository.save(mapper.map(userCreationDto, User.class));
        user.encodePassword(user.getPassword());

        return user;
    }

    public User updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = getUser(id);
        mapper.map(userUpdateDto, user);
        return user;
    }

    public User deleteUser(long id) {
        User user = getUser(id);
        user.delete();
        return user;
    }

    private User getUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if(user.isDeleted()) {
            throw new UserNotFoundException(email);
        }

        return user;
    }
}
