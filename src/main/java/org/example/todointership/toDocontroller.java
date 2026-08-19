package org.example.todointership;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class toDocontroller {
    private final List<Task> tasks = new ArrayList<>(List.of(
            new Task(1, "Learn Spring Boot", false),
            new Task(2, "Build REST API", false),
            new Task(3, "Test the API", true)
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
        return tasks;
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

}
