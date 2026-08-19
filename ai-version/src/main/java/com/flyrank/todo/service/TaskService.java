package com.flyrank.todo.service;

import com.flyrank.todo.dto.CreateTaskRequest;
import com.flyrank.todo.dto.UpdateTaskRequest;
import com.flyrank.todo.exception.BadRequestException;
import com.flyrank.todo.exception.TaskNotFoundException;
import com.flyrank.todo.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 4;

    public TaskService() {
        tasks.add(new Task(1, "Learn HTTP basics", false));
        tasks.add(new Task(2, "Build CRUD API", false));
        tasks.add(new Task(3, "Test with Swagger", true));
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Task getTaskById(long id) {
        return findTask(id);
    }

    public Task createTask(CreateTaskRequest request) {
        Task task = new Task(nextId++, request.getTitle().trim(), false);
        tasks.add(task);
        return task;
    }

    public Task updateTask(long id, UpdateTaskRequest request) {
        if (request.getTitle() == null && request.getDone() == null) {
            throw new BadRequestException("Request body must contain title, done, or both");
        }

        if (request.getTitle() != null && request.getTitle().isBlank()) {
            throw new BadRequestException("Title cannot be blank");
        }

        Task task = findTask(id);

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle().trim());
        }

        if (request.getDone() != null) {
            task.setDone(request.getDone());
        }

        return task;
    }

    public void deleteTask(long id) {
        Task task = findTask(id);
        tasks.remove(task);
    }

    private Task findTask(long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
