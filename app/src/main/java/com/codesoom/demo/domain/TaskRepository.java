package com.codesoom.demo.domain;

import com.codesoom.demo.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

public interface TaskRepository {
    List<Task> findAll();
    Task find(Long id);
    Task save(Task source);
    Task update(long id, Task source);
    Task remove(long id);
}
