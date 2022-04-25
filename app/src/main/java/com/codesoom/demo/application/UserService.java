package com.codesoom.demo.application;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.exceptions.DuplicateUserEmailException;
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

        return userRepository.save(mapper.map(userCreationDto, User.class));
    }
}
