package com.codesoom.demo.domain;

import com.codesoom.demo.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private Long id = 0L;

    public List<Task> findAll() {
        return tasks;
    }

    public Task find(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task save(Task source) {
        Task task = new Task();
        increaseId();
        task.setId(id);
        task.setTitle(source.getTitle());
        tasks.add(task);

        return task;
    }

    public Task update(long id, Task source) {
        Task task = find(id);
        task.setTitle(source.getTitle());
        return task;
    }

    public Task remove(long id) {
        Task task = find(id);
        tasks.remove(find(id));
        return task;
    }

    public void increaseId() {
        this.id = this.id + 1;
    }
}
