package com.codesoom.demo.application;

import com.codesoom.demo.exceptions.TaskNotFoundException;
import com.codesoom.demo.models.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)
    public TaskService() {
        Task.sequence = 0;
    }

    private final List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(Task source) {
        Task task = new Task();

        task.setId(task.generateId());
        task.setTitle(source.getTitle());

        tasks.add(task);
        return task;
    }

    public Task updateTask(Long id, Task source) {
        Task task = getTask(id);
        task.setTitle(source.getTitle());
        return task;
    }

    public void deleteTask(Long id) {
        tasks.remove(getTask(id));
    }
}
