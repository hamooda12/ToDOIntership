package org.example.todointership;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Task {
    private static long counter = 0;
    private  long id=0;
    @NotBlank(message = "Title cannot be blank")

    @Size(max = 100, message = "Title cannot be longer than 100 characters")
    private String title;
    private boolean done;
    public Task(String title) {
        id = ++counter;
        this.title = title;
        done = false;

    }
    public Task() {
    }


    public  long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return done;
    }

    public void  setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
