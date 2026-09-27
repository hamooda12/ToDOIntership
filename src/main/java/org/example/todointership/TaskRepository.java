package org.example.todointership;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Task mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new Task(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getBoolean("done")
        );
    }

    public List<Task> findAll() {
        return jdbcTemplate.query(
                "SELECT id, title, done FROM tasks ORDER BY id",
                this::mapRow
        );
    }

    public List<Task> findByDone(boolean done) {
        return jdbcTemplate.query(
                "SELECT id, title, done FROM tasks WHERE done = ? ORDER BY id",
                this::mapRow,
                done
        );
    }

    public Optional<Task> findById(long id) {
        List<Task> tasks = jdbcTemplate.query(
                "SELECT id, title, done FROM tasks WHERE id = ?",
                this::mapRow,
                id
        );
        return tasks.stream().findFirst();
    }

    public Task create(String title) {
        Task task = jdbcTemplate.queryForObject(
                "INSERT INTO tasks (title, done) VALUES (?, ?) RETURNING id, title, done",
                this::mapRow,
                title,
                false
        );

        if (task == null) {
            throw new IllegalStateException("Task was not created");
        }

        return task;
    }

    public Optional<Task> update(long id, String title, boolean done) {
        int updated = jdbcTemplate.update(
                "UPDATE tasks SET title = ?, done = ? WHERE id = ?",
                title,
                done,
                id
        );

        if (updated == 0) {
            return Optional.empty();
        }

        return findById(id);
    }

    public boolean delete(long id) {
        return jdbcTemplate.update(
                "DELETE FROM tasks WHERE id = ?",
                id
        ) > 0;
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tasks",
                Long.class
        );
        return count == null ? 0 : count;
    }

    public void seed(String title) {
        jdbcTemplate.update(
                "INSERT INTO tasks (title, done) VALUES (?, ?)",
                title,
                false
        );
    }
}
