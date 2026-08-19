package org.example.todointership;


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

    @GetMapping("/tasks")
    public List<Task> getTasks() {
        tasks.get(0).setDone(true);
        return ResponseEntity.ok(tasks).getBody();
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable long id) {
        return ResponseEntity.ok(tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"))).getBody();
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task).getBody();
    }


}
