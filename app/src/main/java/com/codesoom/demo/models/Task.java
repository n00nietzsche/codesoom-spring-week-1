// 다루는 문제 (도메인) 에 대한 패키지로 models 생성
package com.codesoom.demo.models;


public class Task {
    private static long sequence = 0;

    private Long id;
    private String title;

    public long generateId() {
        sequence += 1;
        return sequence;
    }

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