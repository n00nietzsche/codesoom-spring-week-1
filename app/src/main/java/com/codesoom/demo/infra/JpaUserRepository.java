package com.codesoom.demo.infra;

import com.codesoom.demo.domain.User;
import com.codesoom.demo.domain.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
    @Override
    User save(User user);

    @Override
    User findByEmail(String email);

    @Override
    boolean existsByEmail(String email);
}
