package com.codesoom.demo.domain;

import com.codesoom.demo.exceptions.AlreadyDeletedUserException;
import com.codesoom.demo.exceptions.UserNotFoundException;
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
//    @Builder.Default // 빌더 패턴을 사용할 때 특정한 초기값을 기본 값으로 사용하는 것
    private boolean deleted = false;

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

    public void delete() {
        if(deleted) {
            throw new UserNotFoundException(id);
        }

        deleted = true;
    }

    public boolean authenticate(String password) {
        return !deleted && this.password.equals(password);
    }

    public boolean isDeleted() {
        return deleted;
    }
}
