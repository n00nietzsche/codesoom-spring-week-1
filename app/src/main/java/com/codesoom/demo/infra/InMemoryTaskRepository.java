package com.codesoom.demo.infra;

import com.codesoom.demo.domain.Task;
import com.codesoom.demo.domain.TaskRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {

    private final List<Task> tasks = new ArrayList<>();
    private Long id = 0L;

    @Override
    public List<Task> findAll() {
        return tasks;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    @Override
    public Task save(Task source) {
        Task task = new Task();
        increaseId();
        task.setId(id);
        task.setTitle(source.getTitle());
        tasks.add(task);

        return task;
    }

    @Override
    public void delete(Task task) {
        tasks.remove(task);
    }

    public void increaseId() {
        this.id = this.id + 1;
    }
}
