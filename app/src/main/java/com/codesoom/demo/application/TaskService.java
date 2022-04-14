package com.codesoom.demo.application;

import com.codesoom.demo.domain.TaskRepository;
import com.codesoom.demo.domain.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(Long id) {
        return taskRepository.find(id);
    }

    public Task createTask(Task source) {
        return taskRepository.save(source);
    }

    public Task updateTask(Long id, Task source) {
        return taskRepository.update(id, source);
    }

    public Task deleteTask(Long id) {
        return taskRepository.remove(id);
    }
}
