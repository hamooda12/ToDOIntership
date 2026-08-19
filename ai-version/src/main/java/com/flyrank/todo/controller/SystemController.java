package com.flyrank.todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SystemController {

    @Operation(summary = "Show basic API information")
    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
                "name", "Task API",
                "version", "1.0",
                "endpoints", List.of("/tasks")
        );
    }

    @Operation(summary = "Check whether the API is running")
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
