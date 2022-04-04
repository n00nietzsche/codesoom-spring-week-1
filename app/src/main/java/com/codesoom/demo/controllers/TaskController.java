package com.codesoom.demo.controllers;

// DONE: Read Collection - GET /tasks => 완료
// TODO: Read Item - GET /tasks/{id}
// TODO: Create - POST /tasks => 완료
// TODO: Update - PUT/PATCH /tasks/{id}
// TODO: Delete - DELETE /tasks/{id}

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
    public ResponseEntity<Task> detail(@PathVariable Long id) {

        // ResponseEntity.of() 내부 구현이 아래와 같이 되어있다.
        // Assert.notNull(body, "Body must not be null");
        // return body.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
        return ResponseEntity.of(findTask(id));
    }

    @PatchMapping ("{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task source) {
        Optional<Task> entity = findTask(id);

        if(entity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = entity.get();
        task.setTitle(source.getTitle());

        return ResponseEntity.of(entity);
    }

    @DeleteMapping ("{id}")
    public ResponseEntity<Task> delete(@PathVariable Long id) {
        Optional<Task> entity = findTask(id);

        if(entity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        tasks.remove(entity.get());

        // 반환하는 엔티티가 없을 때는 204,
        return ResponseEntity.noContent().build();
    }

    private Optional<Task> findTask(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id))
                .findFirst();
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
