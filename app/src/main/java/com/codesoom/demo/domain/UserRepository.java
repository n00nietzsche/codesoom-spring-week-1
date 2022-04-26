package com.codesoom.demo.domain;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);
}