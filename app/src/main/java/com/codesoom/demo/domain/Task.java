// 다루는 문제 (도메인) 에 대한 패키지로 models 생성
package com.codesoom.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// Entity (Domain)
// 참고: DB 의 Entity 와는 다르다.
// 그러나, 앞으로는 JPA 의 Entity 역할도 같이 하기로 하자.
@Entity
public class Task {
    // Unique Identifier, Unique Key
    @Id
//    @GeneratedValue
    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
