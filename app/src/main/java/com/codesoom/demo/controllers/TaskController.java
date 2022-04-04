package com.codesoom.demo.controllers;

// DONE: Read Collection - GET /tasks => 완료
// TODO: Read Item - GET /tasks/{id}
// TODO: Create - POST /tasks => 완료
// TODO: Update - PUT/PATCH /tasks/{id}
// TODO: Delete - DELETE /tasks/{id}

import com.codesoom.demo.exceptions.TaskNotFoundException;
import com.codesoom.demo.models.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * tasks/{id} 와 같이 주소에서 얻어온 값을 사용한다.
     */
    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {

        // ResponseEntity.of() 내부 구현이 아래와 같이 되어있다.
        // Assert.notNull(body, "Body must not be null");
        // return body.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
        return findTask(id);
    }

    @PatchMapping ("{id}")
    public Task update(@PathVariable Long id, @RequestBody Task source) {
        Task task = findTask(id);
        task.setTitle(source.getTitle());
        return task;
    }

    @DeleteMapping ("{id}")
    public ResponseEntity<Task> delete(@PathVariable Long id) {
        tasks.remove(findTask(id));
        // 처리에 성공했지만, 반환하는 엔티티가 없을 때는 204 상태코드를 이용하자.
        return ResponseEntity.noContent().build();
    }

    private Task findTask(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
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
