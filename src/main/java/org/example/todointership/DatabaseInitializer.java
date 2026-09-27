package org.example.todointership;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final TaskRepository taskRepository;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate, TaskRepository taskRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskRepository = taskRepository;
    }

    @PostConstruct
    public void initialize() {
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS tasks (
                    id BIGSERIAL PRIMARY KEY,
                    title TEXT NOT NULL,
                    done BOOLEAN NOT NULL DEFAULT FALSE
                )
                """);

        if (taskRepository.count() == 0) {
            taskRepository.seed("Learn Spring Boot");
            taskRepository.seed("Build REST API");
            taskRepository.seed("Test the API");
        }
    }
}
