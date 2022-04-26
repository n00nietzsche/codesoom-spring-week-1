package com.codesoom.demo.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

    @Builder
    public User(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void changeWith(User source) {
        this.name = source.name;
        this.password = source.password;
    }
}
