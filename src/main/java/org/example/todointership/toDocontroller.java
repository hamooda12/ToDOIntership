package org.example.todointership;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class toDocontroller {

    private final TaskRepository taskRepository;

    public toDocontroller(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public String hello() {
        return "{ \"name\": \"Task API\", \"version\": \"1.0\", \"endpoints\": [\"/tasks\"] }";
    }

    @GetMapping("/health")
    public String health() {
        return "{ \"status\": \"ok\" }";
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) Boolean done) {

        if (done == null) {
            return ResponseEntity.ok(taskRepository.findAll());
        }

        return ResponseEntity.ok(taskRepository.findByDone(done));
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable long id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        Task createdTask = taskRepository.create(task.getTitle());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTask);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable long id,
            @RequestBody @Valid Task updatedTask) {

        Task task = taskRepository.update(
                id,
                updatedTask.getTitle(),
                updatedTask.isDone()
        ).orElseThrow(() -> new TaskNotFoundException(id));

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        if (!taskRepository.delete(id)) {
            throw new TaskNotFoundException(id);
        }

        return ResponseEntity.noContent().build();
    }
}
