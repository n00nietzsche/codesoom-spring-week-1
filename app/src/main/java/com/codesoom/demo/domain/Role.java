package com.codesoom.demo.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Builder
    public Role(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String name;
}
