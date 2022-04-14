package com.codesoom.demo.infra;

import com.codesoom.demo.domain.Task;
import com.codesoom.demo.domain.TaskRepository;
import com.codesoom.demo.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private final List<Task> tasks = new ArrayList<>();
    private Long id = 0L;

    @Override
    public List<Task> findAll() {
        return tasks;
    }

    @Override
    public Task find(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
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
    public Task update(long id, Task source) {
        Task task = find(id);
        task.setTitle(source.getTitle());
        return task;
    }

    @Override
    public Task remove(long id) {
        Task task = find(id);
        tasks.remove(find(id));
        return task;
    }

    public void increaseId() {
        this.id = this.id + 1;
    }
}
