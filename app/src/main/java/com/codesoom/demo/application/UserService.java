package com.codesoom.demo.application;

import com.codesoom.demo.domain.Role;
import com.codesoom.demo.domain.RoleRepository;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import com.codesoom.demo.dto.UserCreationDto;
import com.codesoom.demo.dto.UserUpdateDto;
import com.codesoom.demo.exceptions.DuplicateUserEmailException;
import com.codesoom.demo.exceptions.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User createUser(UserCreationDto userCreationDto) {
        if(userRepository.existsByEmail(userCreationDto.getEmail())) {
            throw new DuplicateUserEmailException(userCreationDto.getEmail());
        }

        User user = userRepository.save(mapper.map(userCreationDto, User.class));
        user.encodePassword(user.getPassword());

        Role role = new Role(user.getId(), "USER");
        roleRepository.save(role);

        return user;
    }

    public User updateUser(Long id, UserUpdateDto userUpdateDto, Long userId) {
        if(!id.equals(userId)) {
            throw new AccessDeniedException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

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
