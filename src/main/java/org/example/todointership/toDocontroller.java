package org.example.todointership;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class toDocontroller {

    List<Task> tasks = new ArrayList<>(List.of(
            new Task("Learn Spring Boot"),
            new Task("Build REST API"),
            new Task("Test the API")

    ));


    @GetMapping()
    public  String hello(){
        return "{ \"name\": \"Task API\", \"version\": \"1.0\", \"endpoints\": [\"/tasks\"] }";
    }

    @GetMapping("/health")
    public String health() {
        return "{ \"status\": \"ok\" }";  }

    @GetMapping("/tasks?done={done}")
    public ResponseEntity<List<Task>> getTasksByDone(@PathVariable boolean done) {
        List<Task> filteredTasks = tasks.stream()
                .filter(task -> task.isDone() == done)
                .toList();
        return ResponseEntity.ok(filteredTasks);
    }
    public ResponseEntity<List<Task>> getTasks() {

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable long id) {
        return ResponseEntity.ok(tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable long id,
            @RequestBody @Valid Task updatedTask) {

        Task task = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Task not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDone(updatedTask.isDone());

        return ResponseEntity.ok(task);
    }
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        if(tasks.stream().noneMatch(task -> task.getId() == id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); }

    tasks.removeIf(task -> task.getId() == id);
    return ResponseEntity.noContent().build(); }


}
