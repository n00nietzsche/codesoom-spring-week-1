package com.codesoom.demo.domain;

public interface UserRepository {
    User save(User user);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
