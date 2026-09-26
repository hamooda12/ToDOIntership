package org.example.todointership;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Task {

    private long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title cannot be longer than 100 characters")
    private String title;

    private boolean done;

    public Task() {
    }

    public Task(long id, String title, boolean done) {
        this.id = id;
        this.title = title;
        this.done = done;
    }

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
