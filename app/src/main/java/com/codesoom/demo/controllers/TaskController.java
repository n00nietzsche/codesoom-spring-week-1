package com.codesoom.demo.controllers;

// DONE: Read Collection - GET /tasks => 완료
// TODO: Read Item - GET /tasks/{id}
// TODO: Create - POST /tasks => 완료
// TODO: Update - PUT/PATCH /tasks/{id}
// TODO: Delete - DELETE /tasks/{id}

import com.codesoom.demo.application.TaskService;
import com.codesoom.demo.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    // 객체지향 처리에서 위임
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("")
    public List<Task> list() {
        return taskService.getTasks();
    }

    /**
     * tasks/{id} 와 같이 주소에서 얻어온 값을 사용한다.
     */
    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        // ResponseEntity.of() 내부 구현이 아래와 같이 되어있다.
        // Assert.notNull(body, "Body must not be null");
        // return body.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
        return taskService.getTask(id);
    }

    @PatchMapping ("{id}")
    public Task update(@PathVariable Long id, @RequestBody Task source) {
        return taskService.updateTask(id, source);
    }

    @DeleteMapping ("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PostMapping("")
    // HTTP Body 에 존재하는 JSON 자동으로 파싱하기
    public Task create(@RequestBody Task task) {
        return taskService.createTask(task);
    }
}
