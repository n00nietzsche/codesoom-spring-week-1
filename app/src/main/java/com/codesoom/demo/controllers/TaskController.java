package com.codesoom.demo.controllers;

// TODO: Read Collection - GET /tasks => 완료
// TODO: Read Item - GET /tasks/{id}
// TODO: Create - POST /tasks => 완료
// TODO: Update - PUT/PATCH /tasks/{id}
// TODO: Delete - DELETE /tasks/{id}

import com.codesoom.demo.models.Task;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    @GetMapping("")
    public List<Task> list() {
        return tasks;
    }

    @PostMapping("")
    // HTTP Body 에 존재하는 JSON 자동으로 파싱하기
    public Task create(@RequestBody Task task) {
        if (task.getTitle().isBlank()) {
            // TODO: ERROR 처리, Spring Validation
        }

        task.setId(task.generateId());
        tasks.add(task);
        return task;
    }
}
