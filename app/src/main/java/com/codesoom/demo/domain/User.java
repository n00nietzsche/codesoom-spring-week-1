package com.codesoom.demo.domain;

import com.codesoom.demo.exceptions.UserNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @JsonIgnore
    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Builder
    public User(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        encodePassword(password);
    }

    public void changeWith(User source) {
        this.name = source.name;
    }

    public void delete() {
        if(deleted) {
            throw new UserNotFoundException(id);
        }

        deleted = true;
    }

    /**
     * 아이디의 deleted 속성이 false 이며,
     * 입력한 패스워드가 일치하면 인증 성공
     * @param password - 암호 문자열
     * @return isAuthenticated - 인증 성공 했다면 true 아니면 false
     */
    public boolean authenticate(String password) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void encodePassword(String password) {
        this.password = passwordEncoder.encode(password);
    }
}
